package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.constant.UserValidatePatternConstant;
import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.enumeration.ReStatusEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.exception.UserValidateException;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReRole;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.mapper.system.ReUserMapper;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import cn.ljtnono.re.security.util.ReJwtUtil;
import cn.ljtnono.re.vo.system.ReUserLoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author ljt
 * Date: 2020/8/2 0:50
 * Description: 用户服务类
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

    //*********************************** 增删改查 ***********************************//

    /**
     * 新增用户接口
     * @param reUserDTO 用户参数封装
     * @return ReJsonResultVO<?>
     */
    public ReJsonResultVO<?> addUser(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.REQUEST_PARAM_ERROR));
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
            throw new GlobalException(ReErrorEnum.SYSTEM_ERROR);
        }
        reUserDTO.setId(reUser.getId());
        Integer result = reUserMapper.insertUserRole(reUserDTO);
        if (result == null || result <= 0) {
            throw new GlobalException(ReErrorEnum.SYSTEM_ERROR);
        }
        return ReJsonResultVO.success(null);
    }

    /**
     * 根据id删除一个用户
     * @param id 用户id
     */
    public void deleteUserById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.USER_ID_NULL_ERROR));
        int update = reUserMapper.update(null, new UpdateWrapper<ReUser>().lambda()
                .set(ReUser::getDeleted, 1)
                .eq(ReUser::getId, id));
        if (update <= 0) {
            throw new GlobalException(ReErrorEnum.SYSTEM_ERROR);
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
            throw new GlobalException(ReErrorEnum.SYSTEM_ERROR);
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
        IPage<ReUser> result = reUserMapper.selectPage(page, wrapper);
        return result;
    }

    /**
     * 登录
     * @param reUserDTO 参数封装
     * @return ReUserLoginVO
     */
    public ReUserLoginVO login(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 校验用户名和密码
        dtoUsernameAndPasswordCheck(reUserDTO);
        // 校验验证码
        verifyCodeValidate(reUserDTO.getVerifyCodeId(), reUserDTO.getVerifyCode());
        // 生成登陆凭证
        ReUser user = authenticate(reUserDTO.getUsername());
        // 生成token
        String token = generateToken(user);
        // TODO 权限树、角色Id、角色名
        ReUserLoginVO vo = new ReUserLoginVO();
        BeanUtils.copyProperties(user, vo);
        vo.setToken(token);
        return vo;
    }

    /**
     * 登陆认证
     * @param username
     * @return ReUser
     */
    private ReUser authenticate(String username) {
        ReUser user = (ReUser) loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        return user;
    }

    /**
     * 根据用户对象生成token
     * @param reUser 用户对象, 不能为null，id 和 username字段也不能为null
     * @return token字符串
     */
    private String generateToken(ReUser reUser) {
        return reJwtUtil.generateToken(reUser);
    }

    //*********************************** 业务方法 ***********************************//

    /**
     * 设置创建时间和最后修改时间
     * @param reUserDTO
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
                || !UserValidatePatternConstant.EMAIL_VALIDATE_PATTERN.matcher(reUserDTO.getEmail()).matches()) {
            throw new UserValidateException(ReErrorEnum.EMAIL_FORMAT_ERROR);
        }
        // 手机校验
        if (StringUtils.isEmpty(reUserDTO.getPhone())
                || !UserValidatePatternConstant.PHONE_VALIDATE_PATTERN.matcher(reUserDTO.getPhone()).matches()) {
            throw new UserValidateException(ReErrorEnum.PHONE_FORMAT_ERROR);
        }
    }

    /**
     * 检验dto的用户名和密码
     * @param reUserDTO
     */
    private void dtoUsernameAndPasswordCheck(ReUserDTO reUserDTO) {
        // 用户名校验
        if (StringUtils.isEmpty(reUserDTO.getUsername())
                || !UserValidatePatternConstant.USERNAME_VALIDATE_PATTERN.matcher(reUserDTO.getUsername()).matches()) {
            throw new UserValidateException(ReErrorEnum.USERNAME_FORMAT_ERROR);
        }
        // 密码校验
        if (StringUtils.isEmpty(reUserDTO.getPassword())
                || !UserValidatePatternConstant.PASSWORD_VALIDATE_PATTERN.matcher(reUserDTO.getPassword()).matches()) {
            throw new UserValidateException(ReErrorEnum.PASSWORD_FORMAT_ERROR);
        }
    }

    /**
     * 用户名重复校验
     * @throws UserValidateException 当用户名重复时抛出此异常
     * @param reUserDTO 参数封装
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

    //*********************************** 公共方法 ***********************************//

    /**
     * 验证码校验
     * @param verifyCodeId 验证码在redis中的键
     * @param verifyCode 验证码的值
     */
    public void verifyCodeValidate(String verifyCodeId, String verifyCode) {
        Optional.ofNullable(verifyCodeId)
                .orElseThrow(() -> new UserValidateException(ReErrorEnum.VERIFY_CODE_ERROR));
        Optional.ofNullable(verifyCode)
                .orElseThrow(() -> new UserValidateException(ReErrorEnum.VERIFY_CODE_ERROR));
        // 获取code
        Object o = redisUtil.get(verifyCodeId);
        Optional.ofNullable(o)
                .orElseThrow(() -> new UserValidateException(ReErrorEnum.VERIFY_CODE_EXPIRED));
        String code = (String) o;
        if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(verifyCode)) {
            throw new UserValidateException(ReErrorEnum.VERIFY_CODE_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws GlobalException {
        // 根据用户名查出用户所有权限
        ReUser reUser = reUserMapper.selectOne(new QueryWrapper<ReUser>().lambda()
                // 获取全部字段
                .select(ReUser.class, tableFieldInfo -> true)
                .eq(ReUser::getUsername, s)
                .eq(ReUser::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
        // 用户不存在
        Optional.ofNullable(reUser)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.USER_NOT_EXIST));
        List<RePermission> permission = reUserMapper.getPermissionExpressionListByUserId(reUser.getId());
        reUser.setAuthorities(permission);
        return reUser;
    }
}
