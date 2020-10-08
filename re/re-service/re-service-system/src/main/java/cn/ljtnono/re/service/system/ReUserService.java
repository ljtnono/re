package cn.ljtnono.re.service.system;

import cn.ljtnono.re.cache.ReUserInfoCache;
import cn.ljtnono.re.common.constant.UserValidatePatternConstant;
import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.enumeration.ReRedisKeyEnum;
import cn.ljtnono.re.common.enumeration.ReStatusEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceAlreadyExistException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.businese.BusinessException;
import cn.ljtnono.re.common.exception.security.UserPermissionException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.common.util.EncryptUtil;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReRole;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.mapper.system.ReUserMapper;
import cn.ljtnono.re.security.util.ReJwtUtil;
import cn.ljtnono.re.vo.system.ReUserLoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/2 0:50
 * Description: 用户Service类
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
public class ReUserService implements UserDetailsService {

    @Resource
    private ReUserMapper reUserMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ReJwtUtil reJwtUtil;
    @Autowired
    private ReRoleService reRoleService;
    @Autowired
    private ReRolePermissionService reRolePermissionService;
    @Autowired
    private ReSecurityProperties reSecurityProperties;
    @Autowired
    private ReUserRoleService reUserRoleService;

    //*********************************** 接口方法 ***********************************//

    /**
     * 用户登录
     * @param reUserDTO 参数封装
     * @return ReUserLoginVO
     * @author Ling, Jiatong
     */
    public ReUserLoginVO login(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 登陆校验用户名密码
        ReUser reUser = loginCheckUsernameAndPassword(reUserDTO.getUsername(), reUserDTO.getPassword());
        // 校验验证码
        verifyCodeValidate(reUserDTO.getVerifyCodeId(), reUserDTO.getVerifyCode());
        // 用户角色校验
        ReRole reRole = reRoleService.getRoleIdAndNameByUserId(reUser.getId());
        // 用户权限异常
        Optional.ofNullable(reRole)
                .orElseThrow(() -> new UserPermissionException(ReErrorEnum.USER_PERMISSION_ERROR));
        reUser.setRoleId(reRole.getId());
        reUser.setRoleName(reRole.getName());
        // 用户登录状态校验
        boolean isLogin = isLogin(reUser.getId(), reUser.getUsername());
        // 如果已经登录
        if (isLogin) {
            // 用户已经登录
            throw new BusinessException(ReErrorEnum.USER_ALREADY_LOGIN_ERROR);
        }
        // 获取权限信息
        List<Integer> permission = reRolePermissionService.getRePermissionIdListByReUserId(reUser.getId());
        // 登录认证
        authenticate(reUser);
        // 生成token
        String token = reJwtUtil.generateToken(reUser.getId(), reUser.getUsername(), reUser.getRoleId());
        // 缓存用户信息
        setReUserInfoCache(reUser, token);
        // 生成vo对象
        return generateReUserLoginVO(reUser, token, permission);
    }

    /**
     * 用户登出
     * @param reUser 当前用户
     * @author Ling, Jiatong
     */
    public void logout(ReUser reUser) {
        // 删除用户信息缓存
        if (reUser != null) {
            redisUtil.delete(ReRedisKeyEnum.USER_INFO_KEY.getValue()
                    .replace("id", String.valueOf(reUser.getId()))
                    .replace("username", reUser.getUsername()));
        }
    }

    /**
     * 新增用户接口
     * @param reUserDTO 用户参数封装
     */
    public void addUser(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 基础校验
        userDtoBaseValidate(reUserDTO);
        // 用户名重复校验
        if (isUsernameExist(reUserDTO.getUsername())) {
            throw new ResourceAlreadyExistException(ReErrorEnum.USERNAME_ALREADY_EXIST);
        }
        // 验证码校验
        verifyCodeValidate(reUserDTO.getVerifyCodeId(), reUserDTO.getVerifyCode());
        // 检查角色是否存在
        boolean roleExist = reRoleService.isExistById(reUserDTO.getRoleId());
        if (!roleExist) {
            // 角色不存在
            throw new ResourceNotExistException(ReErrorEnum.ROLE_NOT_EXIST);
        }
        // 构建用户实体和角色实体，插入到相应的表中去
        ReUser reUser = new ReUser();
        BeanUtils.copyProperties(reUserDTO, reUser);
        reUser.setDeleted(ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        setCreateTimeAndModifyTimeNow(reUser);
        int insert = reUserMapper.insert(reUser);
        if (insert <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
        reUserDTO.setId(reUser.getId());
        int result = reUserMapper.insertUserRole(reUserDTO);
        if (result <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    /**
     * 根据id删除一个用户, 逻辑删除
     * @param id 用户id
     */
    public void logicDeleteById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.USER_ID_NULL_ERROR));
        // 查看是否存在此用户
        boolean exist = isExistById(id);
        if (exist) {
            int update = reUserMapper.update(null, new LambdaUpdateWrapper<ReUser>()
                    .set(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_DELETED)
                    .set(ReUser::getModifyTime, new Date())
                    .eq(ReUser::getId, id));
            if (update <= 0) {
                throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
            }
        }
    }

    /**
     * 修改用户信息
     * @param reUserDTO 用户参数封装
     * @author Ling, Jiatong
     */
    public void updateUser(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 基础校验
        userDtoBaseValidate(reUserDTO);
        // 是否存在此用户
        boolean userExist = isExistById(reUserDTO.getId());
        if (!userExist) {
            throw new ResourceNotExistException(ReErrorEnum.USER_NOT_EXIST);
        }
        // 检查角色是否存在
        boolean roleExist = reRoleService.isExistById(reUserDTO.getRoleId());
        if (!roleExist) {
            throw new ResourceNotExistException(ReErrorEnum.ROLE_NOT_EXIST);
        }
        // 更新用户
        ReUser user = new ReUser();
        BeanUtils.copyProperties(reUserDTO, user);
        user.setPassword(EncryptUtil.getInstance().getMd5LowerCase(reUserDTO.getPassword()));
        user.setModifyTime(new Date());
        // 更新用户角色表
        reUserRoleService.updateRoleIdByUserId(user.getId(), user.getRoleId());
        // 更新用户表
        int update = reUserMapper.updateById(user);
        if (update <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    /**
     * 分页获取用户信息
     * @return IPage<ReUser>
     */
    @Transactional(readOnly = true)
    public IPage<ReUser> getUserListPage(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR));
        Page<ReUser> page = new Page<>(reUserDTO.getPageNum(), reUserDTO.getPageSize());
        LambdaQueryWrapper<ReUser> wrapper = new LambdaQueryWrapper<>();
        // 除了password字段
        wrapper.select(ReUser.class, i -> !i.getProperty().startsWith("password"))
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED);
        if (!StringUtils.isEmpty(reUserDTO.getSearchCondition())) {
            wrapper.like(ReUser::getUsername, reUserDTO.getSearchCondition())
                    .or()
                    .like(ReUser::getEmail, reUserDTO.getSearchCondition())
                    .or()
                    .like(ReUser::getPhone, reUserDTO.getSearchCondition());
        }
        return reUserMapper.selectPage(page, wrapper);
    }

    //*********************************** 私有方法 ***********************************//

    /**
     * 缓存用户信息
     * @param reUser 用户对象
     * @param token 用户token
     * @author Ling, Jiatong
     *
     */
    private void setReUserInfoCache(ReUser reUser, final String token) {
        ReUserInfoCache reUserInfoCache = new ReUserInfoCache();
        reUserInfoCache.setUserId(reUser.getId());
        reUserInfoCache.setUsername(reUser.getUsername());
        reUserInfoCache.setRoleId(reUser.getRoleId());
        reUserInfoCache.setToken(token);
        reUserInfoCache.setLoginDate(new Date());
        // 缓存时间为token过期时间
        redisUtil.set(ReRedisKeyEnum.USER_INFO_KEY.getValue()
                        .replace("id", String.valueOf(reUser.getId()))
                        .replace("username", reUser.getUsername()),
                reUserInfoCache, reSecurityProperties.getTokenExpire(), TimeUnit.HOURS);
    }

    /**
     * 根据用户对象，生成返回页面的vo对象
     * @param reUser 用户对象
     * @param token token
     * @return ReUserLoginVO
     */
    private ReUserLoginVO generateReUserLoginVO(ReUser reUser, String token, List<Integer> permission) {
        ReUserLoginVO vo = new ReUserLoginVO();
        BeanUtils.copyProperties(reUser, vo);
        vo.setPermissionIdList(permission);
        vo.setToken(token);
        return vo;
    }

    /**
     * 设置创建时间和最后修改时间
     * @param reUser 设置创建时间和最后修改时间为现在时间
     */
    private void setCreateTimeAndModifyTimeNow(ReUser reUser) {
        Date now = new Date();
        reUser.setCreateTime(now);
        reUser.setModifyTime(now);
    }

    /**
     * 用户DTO基础校验，主要用于用户注册和添加用户时的校验
     * @param reUserDTO 参数封装
     */
    private void userDtoBaseValidate(ReUserDTO reUserDTO) {
        checkUsernameAndPassword(reUserDTO.getUsername(), reUserDTO.getPassword());
        checkEmailAndPhone(reUserDTO.getEmail(), reUserDTO.getPhone());
    }

    /**
     * 登陆校验用户名和密码
     * @param username 用户名
     * @param password 密码
     * @return ReUser 返回查询出来的用户名和免密
     */
    private ReUser loginCheckUsernameAndPassword(final String username, final String password) {
        // 校验用户名和密码
        if (StringUtils.isEmpty(username)) {
            throw new ParamException(ReErrorEnum.INPUT_USERNAME);
        }
        if (StringUtils.isEmpty(password)) {
            throw new ParamException(ReErrorEnum.INPUT_PASSWORD);
        }
        // 检验是否存在该用户名和密码
        ReUser reUser = reUserMapper.selectOne(new LambdaQueryWrapper<ReUser>()
                .eq(ReUser::getUsername, username)
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
        // 用户不存在
        if (reUser == null) {
            throw new ResourceNotExistException(ReErrorEnum.USER_NOT_EXIST);
        }
        // 密码错误
        if (!reUser.getPassword().equalsIgnoreCase(EncryptUtil.getInstance().getMd5LowerCase(password))) {
            throw new ParamException(ReErrorEnum.PASSWORD_ERROR);
        }
        return reUser;
    }

    /**
     * 登陆认证
     * @param reUser 用户对象
     */
    private void authenticate(ReUser reUser) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(reUser.getUsername(), reUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    /**
     * 邮箱和手机号码校验
     * @param email 邮箱
     * @param phone 手机号
     * @author Ling, Jiatong
     *
     */
    private void checkEmailAndPhone(final String email, final String phone) {
        // 邮箱校验
        if (StringUtils.isEmpty(email)
                || !UserValidatePatternConstant.checkEmail(email)) {
            throw new ParamException(ReErrorEnum.EMAIL_FORMAT_ERROR);
        }
        // 手机校验
        if (StringUtils.isEmpty(phone)
                || !UserValidatePatternConstant.checkPhone(phone)) {
            throw new ParamException(ReErrorEnum.PHONE_FORMAT_ERROR);
        }
    }

    /**
     * 用户名和密码校验
     * @param username 用户名
     * @param password 用户密码
     * @author Ling, Jiatong
     */
    private void checkUsernameAndPassword(final String username, final String password) {
        // 用户名校验
        if (StringUtils.isEmpty(username)
                || !UserValidatePatternConstant.checkUsername(username)) {
            throw new ParamException(ReErrorEnum.USERNAME_FORMAT_ERROR);
        }
        // 密码校验
        if (StringUtils.isEmpty(password)
                || !UserValidatePatternConstant.checkPassword(password)) {
            throw new ParamException(ReErrorEnum.PASSWORD_FORMAT_ERROR);
        }
    }

    /**
     * 验证码校验
     * @param verifyCodeId 验证码在redis中的键
     * @param verifyCode 验证码的值
     */
    private void verifyCodeValidate(final String verifyCodeId, final String verifyCode) {
        if (StringUtils.isEmpty(verifyCodeId)) {
            throw new ParamException(ReErrorEnum.VERIFY_CODE_NULL_ERROR);
        }
        if (StringUtils.isEmpty(verifyCode)) {
            throw new ParamException(ReErrorEnum.VERIFY_CODE_NULL_ERROR);
        }
        // 获取code
        Object o = redisUtil.get(verifyCodeId);
        Optional.ofNullable(o)
                .orElseThrow(() -> new BusinessException(ReErrorEnum.VERIFY_CODE_EXPIRED));
        String code = (String) o;
        if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(verifyCode)) {
            throw new ParamException(ReErrorEnum.VERIFY_CODE_ERROR);
        }
    }

    //*********************************** 公有方法 ***********************************//

    /**
     * 根据用户id获取角色信息
     * @param id 用户id
     * @return ReRole 用户所拥有的角色，如果没有返回null
     */
    public ReRole getRoleById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.USER_ID_NULL_ERROR));
        return reUserMapper.getRoleById(id);
    }

    /**
     * 根据id判断是否角色是否存在
     * @param id 用户id
     * @return 存在返回true，不存在返回false
     * @author Ling, Jiatong
     *
     */
    public boolean isExistById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.USER_ID_NULL_ERROR));
        ReUser reUser = reUserMapper.selectOne(new LambdaQueryWrapper<ReUser>()
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
        return reUser != null;
    }

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     * @author Ling, Jiatong
     */
    @Transactional(readOnly = true)
    public boolean isUsernameExist(final String username) {
        if (StringUtils.isEmpty(username)) {
            throw new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR);
        }
        ReUser reUser = reUserMapper.selectOne(new LambdaQueryWrapper<ReUser>()
                .eq(ReUser::getUsername, username)
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
        return reUser != null;
    }

    /**
     * 根据id获取用户信息
     * @param id 用户id
     * @return ReUser 用户实体, 如果该用户不存在则返回NULL
     */
    @Transactional(readOnly = true)
    public ReUser getUserById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.USER_ID_NULL_ERROR));
        return reUserMapper.selectOne(new LambdaQueryWrapper<ReUser>()
                // 除去密码
                .select(ReUser.class, i -> !i.getProperty().startsWith("password"))
                .eq(ReUser::getId, id)
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
    }

    /**
     * 根据用户id检查用户是否登录
     * @param id 用户id
     * @return 已经登录返回true，没有登陆返回false
     * @author Ling, Jiatong
     */
    public boolean isLogin(Integer id, String username) {
        // 校验登陆状态
        ReUserInfoCache reUserInfoCache = (ReUserInfoCache) redisUtil.get(ReRedisKeyEnum.USER_INFO_KEY.getValue()
                .replace("id", String.valueOf(id))
                .replace("username", username));
        return reUserInfoCache != null;
    }

    //*********************************** 其他方法 ***********************************//

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        // 根据用户名查出用户所有权限
        if (StringUtils.isEmpty(username)) {
            throw new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR);
        }
        ReUser reUser = reUserMapper.selectOne(new LambdaQueryWrapper<ReUser>()
                .eq(ReUser::getUsername, username)
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        // 用户不存在
        Optional.ofNullable(reUser)
                .orElseThrow(() -> new ResourceNotExistException(ReErrorEnum.USER_NOT_EXIST));
        List<RePermission> permission = reUserMapper.getPermissionExpressionListByUserId(reUser.getId());
        reUser.setAuthorities(permission);
        return reUser;
    }
}
