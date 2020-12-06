package cn.ljtnono.re.service.system;

import cn.ljtnono.re.cache.UserInfoCache;
import cn.ljtnono.re.common.constant.system.UserValidatePatternConstant;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.enumeration.RedisKeyEnum;
import cn.ljtnono.re.common.enumeration.StatusEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceAlreadyExistException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.businese.BusinessException;
import cn.ljtnono.re.common.exception.security.UserPermissionException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.common.util.EncryptUtil;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import cn.ljtnono.re.dto.system.UserDTO;
import cn.ljtnono.re.dto.system.UserListQueryDTO;
import cn.ljtnono.re.entity.system.Config;
import cn.ljtnono.re.entity.system.Permission;
import cn.ljtnono.re.entity.system.Role;
import cn.ljtnono.re.entity.system.User;
import cn.ljtnono.re.mapper.system.UserMapper;
import cn.ljtnono.re.security.util.JwtUtil;
import cn.ljtnono.re.vo.system.UserListVO;
import cn.ljtnono.re.vo.system.UserLoginVO;
import cn.ljtnono.re.vo.system.UserRoleNumPieVO;
import cn.ljtnono.re.vo.system.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>用户Service层</p>
 * @author Ling, Jiatong
 * Date: 2020/8/2 0:50
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
public class UserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;
    private final ReSecurityProperties reSecurityProperties;
    private final UserRoleService userRoleService;
    private final ConfigService configService;

    public UserService(RedisUtil redisUtil, JwtUtil jwtUtil, RoleService roleService, RolePermissionService rolePermissionService, ReSecurityProperties reSecurityProperties, UserRoleService userRoleService, ConfigService configService) {
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
        this.rolePermissionService = rolePermissionService;
        this.reSecurityProperties = reSecurityProperties;
        this.userRoleService = userRoleService;
        this.configService = configService;
    }

    //*********************************** 接口方法 ***********************************//

    /**
     * <p>用户登录，分为强行登录和普通登录，强行登录需要校验forceLogin字段为1</p>
     * @param dto 用户通用DTO对象 {@link UserDTO}
     * @return 用户登录返回VO对象 {@link UserLoginVO}
     * @author Ling, Jiatong
     */
    public UserLoginVO login(UserDTO dto) {
        Optional.ofNullable(dto)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        // 登陆校验用户名密码
        User user = loginCheckUsernameAndPassword(dto.getUsername(), dto.getPassword());
        // 校验验证码
        verifyCodeValidate(dto.getVerifyCodeId(), dto.getVerifyCode());
        // 用户角色校验
        Role role = roleService.getRoleIdAndNameByUserId(user.getId());
        // 用户权限异常
        Optional.ofNullable(role)
                .orElseThrow(() -> new UserPermissionException(GlobalErrorEnum.USER_PERMISSION_ERROR));
        user.setRoleId(role.getId());
        user.setRoleName(role.getName());
        // 如果是强行登录
        if (dto.getForceLogin() != null && dto.getForceLogin() == 1) {
            // 下线原来的账号
            deleteUserInfoCache(user.getId(), user.getUsername());
        } else {
            // 用户登录状态校验
            boolean isLogin = isLogin(user.getId(), user.getUsername());
            // 如果已经登录
            if (isLogin) {
                // 用户已经登录
                throw new BusinessException(GlobalErrorEnum.USER_ALREADY_LOGIN_ERROR);
            }
        }
        // 获取权限信息
        List<Integer> permission = rolePermissionService.getPermissionIdListByUserId(user.getId());
        // 登录认证
        authenticate(user);
        // 生成token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoleId());
        // 缓存用户信息
        setUserInfoCache(user, token);
        // 生成vo对象
        return generateUserLoginVO(user, token, permission);
    }

    /**
     * <p>用户登出（删除redis缓存）</p>
     * @param user 当前用户
     * @author Ling, Jiatong
     */
    public void logout(User user) {
        // 删除用户信息缓存
        if (user != null) {
            redisUtil.delete(RedisKeyEnum.USER_INFO_KEY.getValue()
                    .replace("id", String.valueOf(user.getId()))
                    .replace("username", user.getUsername()));
        }
    }

    /**
     * <p>新增用户接口</p>
     * @param dto 用户通用DTO对象 {@link UserDTO}
     * @author Ling, Jiatong
     */
    public void addUser(UserDTO dto) {
        Optional.ofNullable(dto)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        // 基础校验
        userDtoBaseValidate(dto);
        // 用户名重复校验
        if (isUsernameExist(dto.getUsername())) {
            throw new ResourceAlreadyExistException(GlobalErrorEnum.USERNAME_ALREADY_EXIST);
        }
        // 验证码校验
        verifyCodeValidate(dto.getVerifyCodeId(), dto.getVerifyCode());
        // 检查角色是否存在
        roleService.checkExist(dto.getRoleId());
        // 构建用户实体和角色实体，插入到相应的表中去
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(EncryptUtil.getInstance().getMd5LowerCase(dto.getPassword()));
        user.setDeleted(StatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        user.setCreateTime(new Date());
        user.setModifyTime(new Date());
        // 插入相关数据
        int insert = userMapper.insert(user);
        if (insert <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
        dto.setId(user.getId());
        int result = userMapper.insertUserRole(dto);
        if (result <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    /**
     * <p>逻辑删除用户（不删除用户角色关联表数据），用户处于已经删除状态（is_deleted=1）会抛出异常</p>
     * @param dto 用户通用DTO对象 {@link UserDTO}
     * @author Ling, Jiatong
     */
    public void logicDelete(UserDTO dto) {
        Optional.ofNullable(dto)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        if (CollectionUtils.isEmpty(dto.getIdList())) {
            return;
        }
        List<Integer> idList = dto.getIdList();
        // 校验每个用户是否存在，如果不存在那么抛出异常
        idList.parallelStream().forEach(this::checkExist);
        int update = userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getDeleted, StatusEnum.ENTITY_IS_DELETED_DELETED.getValue())
                .set(User::getModifyTime, new Date())
                .in(User::getId, idList));
        if (update <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 如果用户在线的话，那么删除用户redis中的缓存信息
        idList.parallelStream().forEach(id -> {
            String key = RedisKeyEnum.USER_INFO_KEY.getValue()
                    .replace("id", String.valueOf(id))
                    .replace(":username", "") + "*";
            redisUtil.deleteByPattern(key);
        });
    }

    /**
     * <p>修改用户信息，修改后会导致原用户下线（删除用户缓存记录）</p>
     * @param dto 用户通用DTO对象 {@link UserDTO}
     * @author Ling, Jiatong
     */
    public void updateUser(UserDTO dto) {
        Optional.ofNullable(dto)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        // 基础校验
        userDtoBaseValidate(dto);
        // 是否存在此用户
        User oldUser = checkExist(dto.getId());
        // 检查角色是否存在
        roleService.checkExist(dto.getRoleId());
        // 更新用户
        User newUser = new User();
        BeanUtils.copyProperties(dto, newUser);
        newUser.setPassword(EncryptUtil.getInstance().getMd5LowerCase(dto.getPassword()));
        newUser.setModifyTime(new Date());
        // 更新用户角色表
        userRoleService.updateRoleIdByUserId(newUser.getId(), newUser.getRoleId());
        // 更新用户表
        int update = userMapper.updateById(newUser);
        if (update <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 删除之前的用户信息
        deleteUserInfoCache(oldUser.getId(), oldUser.getUsername());
    }

    /**
     * <p>分页获取用户列表</p>
     * @param dto 用户列表查询DTO对象 {@link UserListQueryDTO}
     * @return 用户列表查询VO分页包装对象 {@link UserListVO}
     * @author Ling, Jiatong
     */
    @Transactional(readOnly = true)
    public IPage<UserListVO> getList(UserListQueryDTO dto) {
        Optional.ofNullable(dto)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        // 校验请求参数
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 生成排序参数
        try {
            dto.generateSortCondition();
        } catch (IllegalArgumentException e) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        return userMapper.getList(page, dto);
    }

    /**
     * <p>获取用户根据角色分类统计饼状图</p>
     * @return 用户根据角色分类统计饼状图VO对象 {@link UserRoleNumPieVO}
     * @author Ling, Jiatong
     */
    public List<UserRoleNumPieVO> roleNumPie() {
        return userMapper.roleNumPie();
    }

    //*********************************** 私有方法 ***********************************//


    /**
     * <p>缓存用户信息，根据用户对象生成一个 {@link UserInfoCache} 对象缓存到redis中<br/>
     * 缓存时间为token过期时间</p>
     * @param user 用户对象 {@link User}
     * @param token 用户token
     * @author Ling, Jiatong
     */
    private void setUserInfoCache(User user, final String token) {
        UserInfoCache userInfoCache = new UserInfoCache();
        userInfoCache.setUserId(user.getId());
        userInfoCache.setUsername(user.getUsername());
        userInfoCache.setRoleId(user.getRoleId());
        userInfoCache.setToken(token);
        userInfoCache.setLoginDate(new Date());
        // 缓存时间为token过期时间
        redisUtil.set(RedisKeyEnum.USER_INFO_KEY.getValue()
                        .replace("id", String.valueOf(user.getId()))
                        .replace("username", user.getUsername()),
                userInfoCache, reSecurityProperties.getTokenExpire(), TimeUnit.HOURS);
    }

    /**
     * <p>删除用户redis登录缓存信息</p>
     * @param id 用户id
     * @param username 用户名
     * @author Ling, Jiatong
     */
    private void deleteUserInfoCache(Integer id, String username) {
        redisUtil.delete(RedisKeyEnum.USER_INFO_KEY.getValue()
                        .replace("id", String.valueOf(id))
                        .replace("username", username));
    }

    /**
     * <p>根据用户对象，生成返回页面的vo对象</p>
     * @param user 用户对象 {@link User}
     * @param token token
     * @param permission 用户角色所具有的权限id列表
     * @return 用户登录VO对象 {@link UserLoginVO}
     */
    private UserLoginVO generateUserLoginVO(User user, String token, List<Integer> permission) {
        UserLoginVO vo = new UserLoginVO();
        BeanUtils.copyProperties(user, vo);
        vo.setPermissionIdList(permission);
        vo.setToken(token);
        Config avatarImage = configService.getConfigByKey("avatarImage");
        vo.setAvatarImage(avatarImage.getValue());
        return vo;
    }

    /**
     * <p>用户DTO基础校验，主要用于用户注册和添加用户时的校验</p>
     * @param dto 用户通用DTO对象 {@link UserDTO}
     */
    private void userDtoBaseValidate(UserDTO dto) {
        checkUsernameAndPassword(dto.getUsername(), dto.getPassword());
        checkEmailAndPhone(dto.getEmail(), dto.getPhone());
    }

    /**
     * <p>登陆校验用户名和密码，校验错误会抛出异常</p>
     * @param username 用户名
     * @param password 密码
     * @return 返回查询出来的用户对象
     */
    private User loginCheckUsernameAndPassword(final String username, final String password) {
        // 校验用户名和密码
        if (StringUtils.isEmpty(username)) {
            throw new ParamException(GlobalErrorEnum.INPUT_USERNAME);
        }
        if (StringUtils.isEmpty(password)) {
            throw new ParamException(GlobalErrorEnum.INPUT_PASSWORD);
        }
        // 检验是否存在该用户名和密码
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, StatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        // 用户不存在
        if (user == null) {
            throw new ResourceNotExistException(GlobalErrorEnum.USER_NOT_EXIST);
        }
        // 密码错误
        if (!user.getPassword().equalsIgnoreCase(EncryptUtil.getInstance().getMd5LowerCase(password))) {
            throw new ParamException(GlobalErrorEnum.PASSWORD_ERROR);
        }
        return user;
    }

    /**
     * <p>登陆认证</p>
     * @param user 用户对象 {@link User}
     */
    private void authenticate(User user) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    /**
     * <p>邮箱和手机号码校验</p>
     * @param email 邮箱
     * @param phone 手机号
     * @author Ling, Jiatong
     */
    private void checkEmailAndPhone(final String email, final String phone) {
        // 邮箱校验
        if (StringUtils.isEmpty(email)
                || !UserValidatePatternConstant.checkEmail(email)) {
            throw new ParamException(GlobalErrorEnum.EMAIL_FORMAT_ERROR);
        }
        // 手机校验
        if (StringUtils.isEmpty(phone)
                || !UserValidatePatternConstant.checkPhone(phone)) {
            throw new ParamException(GlobalErrorEnum.PHONE_FORMAT_ERROR);
        }
    }

    /**
     * <p>用户名和密码校验</p>
     * @param username 用户名
     * @param password 用户密码
     * @author Ling, Jiatong
     */
    private void checkUsernameAndPassword(final String username, final String password) {
        // 用户名校验
        if (StringUtils.isEmpty(username)
                || !UserValidatePatternConstant.checkUsername(username)) {
            throw new ParamException(GlobalErrorEnum.USERNAME_FORMAT_ERROR);
        }
        // 密码校验
        if (StringUtils.isEmpty(password)
                || !UserValidatePatternConstant.checkPassword(password)) {
            throw new ParamException(GlobalErrorEnum.PASSWORD_FORMAT_ERROR);
        }
    }

    /**
     * <p>验证码校验</p>
     * @param verifyCodeId 验证码在redis中的键
     * @param verifyCode 验证码的值
     */
    private void verifyCodeValidate(final String verifyCodeId, final String verifyCode) {
        if (StringUtils.isEmpty(verifyCodeId)) {
            throw new ParamException(GlobalErrorEnum.VERIFY_CODE_NULL_ERROR);
        }
        if (StringUtils.isEmpty(verifyCode)) {
            throw new ParamException(GlobalErrorEnum.VERIFY_CODE_NULL_ERROR);
        }
        // 获取code
        Object o = redisUtil.get(verifyCodeId);
        Optional.ofNullable(o)
                .orElseThrow(() -> new BusinessException(GlobalErrorEnum.VERIFY_CODE_EXPIRED));
        String code = (String) o;
        if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(verifyCode)) {
            throw new ParamException(GlobalErrorEnum.VERIFY_CODE_ERROR);
        }
    }

    //*********************************** 公有方法 ***********************************//


    /**
     * <p>校验用户是否存在，如果不存在那么抛出异常，如果存在返回用户实体对象</p>
     * @param id 用户id
     * @return User 用户实体对象 {@link User}
     * @throws ResourceNotExistException 当用户不存在时抛出此异常
     * @author Ling, Jiatong
     */
    public User checkExist(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.USER_ID_NULL_ERROR));
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id)
                .eq(User::getDeleted, StatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        Optional.ofNullable(user)
                .orElseThrow(() -> new ResourceNotExistException(GlobalErrorEnum.USER_NOT_EXIST));
        return user;
    }

    /**
     * <p>根据id判断用户是否存在（不包括已经删除的用户信息）</p>
     * @param id 用户id
     * @return 存在返回true，不存在返回false
     * @author Ling, Jiatong
     */
    public boolean isExistById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.USER_ID_NULL_ERROR));
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id)
                .eq(User::getDeleted, StatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        return user != null;
    }

    /**
     * <p>判断用户名是否存在（不计算已经删除的用户）</p>
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     * @author Ling, Jiatong
     */
    @Transactional(readOnly = true)
    public boolean isUsernameExist(final String username) {
        if (StringUtils.isEmpty(username)) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        User reUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, StatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        return reUser != null;
    }

    /**
     * <p>根据id获取用户信息。<br/>
     * 只获取username、id、email、phone、roleId、roleName字段</p>
     * @param id 用户id
     * @return 用户通用VO对象 {@link UserVO}
     */
    @Transactional(readOnly = true)
    public UserVO getUserById(Integer id) {
        // 校验用户是否存在，如果不存在，那么抛出异常
        checkExist(id);
        return userMapper.getUserById(id);
    }

    /**
     * <p>根据用户id和用户名检查用户是否登录</p>
     * @param id 用户id
     * @param username 用户名
     * @return 已经登录返回true，没有登陆返回false
     * @author Ling, Jiatong
     */
    public boolean isLogin(Integer id, String username) {
        // 校验登陆状态
        UserInfoCache userInfoCache = (UserInfoCache) redisUtil.get(RedisKeyEnum.USER_INFO_KEY.getValue()
                .replace("id", String.valueOf(id))
                .replace("username", username));
        return userInfoCache != null;
    }

    //*********************************** 其他方法 ***********************************//

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        // 根据用户名查出用户所有权限
        if (StringUtils.isEmpty(username)) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        User reUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, StatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        // 用户不存在
        Optional.ofNullable(reUser)
                .orElseThrow(() -> new ResourceNotExistException(GlobalErrorEnum.USER_NOT_EXIST));
        List<Permission> permission = userMapper.getPermissionExpressionListByUserId(reUser.getId());
        reUser.setAuthorities(permission);
        return reUser;
    }

}
