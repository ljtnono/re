package cn.ljtnono.re.service.system;

import cn.ljtnono.re.cache.ReUserInfoCache;
import cn.ljtnono.re.common.constant.UserValidatePatternConstant;
import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.enumeration.ReRedisKeyEnum;
import cn.ljtnono.re.common.enumeration.ReStatusEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.businese.UserValidateException;
import cn.ljtnono.re.common.exception.security.UserPermissionException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.common.util.EncryptUtil;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReRole;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.mapper.system.ReUserMapper;
import cn.ljtnono.re.security.util.ReJwtUtil;
import cn.ljtnono.re.vo.system.ReUserLoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author ljt
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

    //*********************************** 接口方法 ***********************************//

    /**
     * 获取用户下拉列表
     *
     * @return List<User>
     */
    @Transactional(readOnly = true)
    public List<ReUser> select() {
        return reUserMapper.selectList(new LambdaQueryWrapper<ReUser>()
                .select(ReUser::getId, ReUser::getUsername));
    }

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
        ReUser reUserDB = loginCheckUsernameAndPassword(reUserDTO.getUsername(), reUserDTO.getPassword());
        // 校验验证码
        verifyCodeValidate(reUserDTO.getVerifyCodeId(), reUserDTO.getVerifyCode());
        // 用户角色校验
        ReRole reRoleDB = reRoleService.getRoleIdAndNameByUserId(reUserDB.getId());
        // 用户权限异常
        Optional.ofNullable(reRoleDB)
                .orElseThrow(() -> new UserPermissionException(ReErrorEnum.USER_PERMISSION_ERROR));
        reUserDB.setRoleId(reRoleDB.getId());
        reUserDB.setRoleName(reRoleDB.getName());
        // 用户登录状态校验
        loginStatusCheck(reUserDTO, reUserDB);
        // 登录认证
        authenticate(reUserDTO);
        // 生成token
        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", reUserDB.getId());
        map.put("username", reUserDB.getUsername());
        map.put("roleId", reUserDB.getId());
        String token = reJwtUtil.generateToken(map);
        // 缓存用户信息
        setReUserInfoCache(reUserDB, token);
        // 获取权限信息
        List<Integer> permission = reRolePermissionService.getRePermissionIdListByReUserId(reUserDB.getId());
        // 生成vo对象
        return generateReUserLoginVO(reUserDB, token, permission);
    }

    /**
     * 用户登出
     * @param reUser 用户实体
     */
    public void logout(ReUser reUser) {
        // 删除用户信息缓存
        redisUtil.delete(ReRedisKeyEnum.USER_INFO_KEY.getKey()
                .replace("id", reUser.getId() + "")
                .replace("username", reUser.getUsername()));
    }

    /**
     * 新增用户接口
     * @param reUserDTO 用户参数封装
     * @return ReJsonResultVO<?>
     */
    public ReJsonResultVO<?> addUser(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 基础校验
        userDtoBaseValidate(reUserDTO);
        // 用户名重复校验
        usernameDuplicateValidate(reUserDTO.getUsername());
        // 验证码校验
        verifyCodeValidate(reUserDTO.getVerifyCodeId(), reUserDTO.getVerifyCode());
        // 构建用户实体和角色实体，插入到相应的表中去
        ReUser reUser = new ReUser();
        BeanUtils.copyProperties(reUserDTO, reUser);
        reUser.setDeleted(ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        setCreateTimeAndModifyTime(reUser);
        int insert = reUserMapper.insert(reUser);
        if (insert <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
        reUserDTO.setId(reUser.getId());
        int result = reUserMapper.insertUserRole(reUserDTO);
        if (result <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
        return ReJsonResultVO.success();
    }

    /**
     * 根据id删除一个用户, 逻辑删除
     * @param id 用户id
     */
    public void deleteUserById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.USER_ID_NULL_ERROR));
        int update = reUserMapper.update(null, new LambdaUpdateWrapper<ReUser>()
                .set(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_DELETED.getValue())
                .eq(ReUser::getId, id));
        if (update <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    /**
     * 修改用户信息
     * @param reUserDTO
     */
    public void updateUser(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.REQUEST_PARAM_ERROR));
        Optional.ofNullable(reUserDTO.getId())
                .orElseThrow(() -> new GlobalException(ReErrorEnum.USER_ID_NULL_ERROR));
        // 基础校验
        userDtoBaseValidate(reUserDTO);
        // 是否存在此用户
        ReUser user = getUserById(reUserDTO.getId());
        Optional.ofNullable(user)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.USER_NOT_EXIST));
        BeanUtils.copyProperties(reUserDTO, user);
        user.setModifyTime(new Date());
        int update = reUserMapper.updateById(user);
        if (update <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    /**
     * 根据id获取用户信息
     * @param id 用户id
     * @return ReUser 用户实体, 如果该用户不存在则返回NULL
     */
    @Transactional(readOnly = true)
    public ReUser getUserById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.USER_ID_NULL_ERROR));
        return reUserMapper.selectOne(new QueryWrapper<ReUser>().lambda()
                // 除去密码
                .select(ReUser.class, i -> !i.getProperty().startsWith("password"))
                .eq(ReUser::getId, id));
    }

    /**
     * 分页获取用户信息
     * @return IPage<ReUser>
     */
    @Transactional(readOnly = true)
    public IPage<ReUser> getUserListPage(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.REQUEST_PARAM_ERROR));
        Page<ReUser> page = new Page<>(reUserDTO.getPageNum(), reUserDTO.getPageSize());
        LambdaQueryWrapper<ReUser> wrapper = new LambdaQueryWrapper<>();
        // 除了password字段
        wrapper.select(ReUser::getId,
                ReUser::getUsername,
                ReUser::getPhone,
                ReUser::getEmail,
                ReUser::getCreateTime,
                ReUser::getModifyTime,
                ReUser::getDeleted)
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        // TODO 此处如果多个条件都存在，可能会出现问题，需要详细测试
        if (!StringUtils.isEmpty(reUserDTO.getUsername())) {
            wrapper.like(ReUser::getUsername, reUserDTO.getUsername());
        }
        if (!StringUtils.isEmpty(reUserDTO.getEmail())) {
            wrapper.like(ReUser::getEmail, reUserDTO.getEmail());
        }
        if (!StringUtils.isEmpty(reUserDTO.getPhone())) {
            wrapper.like(ReUser::getPhone, reUserDTO.getPhone());
        }
        return reUserMapper.selectPage(page, wrapper);
    }



    /**
     * 缓存用户信息
     * @param reUserDB 用户对象
     * @param token 用户token
     * @author Ling, Jiatong
     *
     */
    private void setReUserInfoCache(ReUser reUserDB, final String token) {
        ReUserInfoCache reUserInfoCache = new ReUserInfoCache();
        reUserInfoCache.setUserId(reUserDB.getId());
        reUserInfoCache.setUsername(reUserDB.getUsername());
        reUserInfoCache.setRoleId(reUserDB.getRoleId());
        reUserInfoCache.setToken(token);
        reUserInfoCache.setLoginDate(new Date());
        // 缓存12小时
        redisUtil.set(ReRedisKeyEnum.USER_INFO_KEY.getKey()
                .replace("id", reUserDB.getId() + "")
                .replace("username", reUserDB.getUsername()),
                reUserInfoCache, 12L, TimeUnit.HOURS);
    }

    /**
     * 用户登录状态校验
     *
     * @param reUserDTO 用户登录数据传输对象
     * @param reUserDB  用户实体
     */
    private void loginStatusCheck(ReUserDTO reUserDTO, ReUser reUserDB) {
        // TODO 用户是否已经登录校验
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
     * 登陆校验用户名和密码
     * @param username 用户名
     * @param password 密码
     * @return ReUser 返回查询出来的用户名和免密
     */
    private ReUser loginCheckUsernameAndPassword(final String username, final String password) {
        // 校验用户名和密码
        if (StringUtils.isEmpty(username)) {
            throw new UserValidateException(ReErrorEnum.INPUT_USERNAME);
        }
        if (StringUtils.isEmpty(password)) {
            throw new UserValidateException(ReErrorEnum.INPUT_PASSWORD);
        }
        // 检验是否存在该用户名和密码
        ReUser reUser = reUserMapper.selectOne(new LambdaQueryWrapper<ReUser>()
                .eq(ReUser::getUsername, username)
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        // 用户不存在
        if (reUser == null) {
            throw new UserValidateException(ReErrorEnum.USER_NOT_EXIST);
        }
        // 密码错误
        if (!reUser.getPassword().equalsIgnoreCase(EncryptUtil.getInstance().getMd5LowerCase(password))) {
            throw new UserValidateException(ReErrorEnum.PASSWORD_ERROR);
        }
        return reUser;
    }

    /**
     * 登陆认证
     * @param reUserDTO 用户登录参数封装
     */
    private void authenticate(ReUserDTO reUserDTO) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(reUserDTO.getUsername(), reUserDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    /**
     * 根据用户对象生成token
     * @param reUser 用户对象, 不能为null，id 和 username字段也不能为null
     * @return token字符串
     */
    private String generateToken(ReUser reUser) {
        return reJwtUtil.generateToken(reUser);
    }

    //*********************************** 私有方法 ***********************************//

    /**
     * 设置创建时间和最后修改时间
     * @param reUser 设置创建时间和最后修改时间为现在时间
     */
    private void setCreateTimeAndModifyTime(ReUser reUser) {
        Date now = new Date();
        reUser.setCreateTime(now);
        reUser.setModifyTime(now);
    }

    /**
     * 用户DTO基础校验，主要用于用户注册和添加用户时的校验
     * @param reUserDTO 参数封装
     */
    public void userDtoBaseValidate(ReUserDTO reUserDTO) {
        dtoUsernameAndPasswordCheck(reUserDTO);
        // 邮箱校验
        if (StringUtils.isEmpty(reUserDTO.getEmail())
                || !UserValidatePatternConstant.REGEX_EMAIL.matcher(reUserDTO.getEmail()).matches()) {
            throw new UserValidateException(ReErrorEnum.EMAIL_FORMAT_ERROR);
        }
        // 手机校验
        if (StringUtils.isEmpty(reUserDTO.getPhone())
                || !UserValidatePatternConstant.REGEX_PHONE.matcher(reUserDTO.getPhone()).matches()) {
            throw new UserValidateException(ReErrorEnum.PHONE_FORMAT_ERROR);
        }
    }

    /**
     * 检验dto的用户名和密码
     * @param reUserDTO dto
     */
    private void dtoUsernameAndPasswordCheck(ReUserDTO reUserDTO) {
        // 用户名校验
        if (StringUtils.isEmpty(reUserDTO.getUsername())
                || !UserValidatePatternConstant.checkUsername(reUserDTO.getUsername())) {
            throw new UserValidateException(ReErrorEnum.USERNAME_FORMAT_ERROR);
        }
        // 密码校验
        if (StringUtils.isEmpty(reUserDTO.getPassword())
                || !UserValidatePatternConstant.checkPassword(reUserDTO.getPassword())) {
            throw new UserValidateException(ReErrorEnum.PASSWORD_FORMAT_ERROR);
        }
    }

    /**
     * 用户名重复校验
     * @throws UserValidateException 当用户名重复时抛出此异常
     * @param username 用户名
     */
    @Transactional(readOnly = true)
    public void usernameDuplicateValidate(String username) {
        // 这里选择的是未删除的用户中不会重名的
        ReUser reUser = reUserMapper.selectOne(new QueryWrapper<ReUser>().lambda()
                .eq(ReUser::getUsername, username));
        if (reUser != null && reUser.getUsername().equals(username)) {
            throw new UserValidateException(ReErrorEnum.USERNAME_ALREADY_EXIST);
        }
    }

    //*********************************** 公有方法 ***********************************//

    /**
     * 验证码校验
     * @param verifyCodeId 验证码在redis中的键
     * @param verifyCode 验证码的值
     */
    public void verifyCodeValidate(final String verifyCodeId, final String verifyCode) {
        if (!StringUtils.isEmpty(verifyCodeId)) {
            throw new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR);
        }
        if (!StringUtils.isEmpty(verifyCode)) {
            throw new ParamException(ReErrorEnum.VERIFY_CODE_NULL_ERROR);
        }
        // 获取code
        Object o = redisUtil.get(verifyCodeId);
        Optional.ofNullable(o)
                .orElseThrow(() -> new UserValidateException(ReErrorEnum.VERIFY_CODE_EXPIRED));
        String code = (String) o;
        if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(verifyCode)) {
            throw new UserValidateException(ReErrorEnum.VERIFY_CODE_ERROR);
        }
    }

    /**
     * 根据用户id获取角色信息
     * @param id 用户id
     * @return ReRole 用户所拥有的角色，如果没有返回null
     */
    public ReRole getRoleById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.USER_ID_NULL_ERROR));
        return reUserMapper.getRoleById(id);
    }

    //*********************************** 其他方法 ***********************************//

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) {
        // 根据用户名查出用户所有权限
        ReUser reUser = reUserMapper.selectOne(new LambdaQueryWrapper<ReUser>()
                // 获取全部字段
                .select(ReUser.class, tableFieldInfo -> true)
                .eq(ReUser::getUsername, s)
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        // 用户不存在
        Optional.ofNullable(reUser)
                .orElseThrow(() -> new ResourceNotExistException(ReErrorEnum.USER_NOT_EXIST));
        List<RePermission> permission = reUserMapper.getPermissionExpressionListByUserId(reUser.getId());
        reUser.setAuthorities(permission);
        return reUser;
    }
}
