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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        usernameDuplicateValidate(reUserDTO);
        // 验证码校验
        verifyCodeValidate(reUserDTO);
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
     * 验证码校验
     * @param reUserDTO 参数封装
     */
    public void verifyCodeValidate(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO.getVerifyCodeId())
                .orElseThrow(() -> new UserValidateException(ReErrorEnum.REQUEST_PARAM_ERROR));
        Optional.ofNullable(reUserDTO.getVerifyCodeId())
                .orElseThrow(() -> new UserValidateException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 获取code
        String code = (String) redisUtil.get(reUserDTO.getVerifyCodeId());
        if (code == null || !code.equalsIgnoreCase(reUserDTO.getVerifyCode())) {
            throw new UserValidateException(ReErrorEnum.VERIFY_CODE_ERROR);
        }
    }

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
     * 用户名重复校验
     * @param reUserDTO 参数封装
     */
    public void usernameDuplicateValidate(ReUserDTO reUserDTO) {
        // 这里选择的是未删除的用户中不会重名的
        ReUser reUser = reUserMapper.selectOne(new QueryWrapper<ReUser>().lambda()
                .select(ReUser.class, i -> true)
                .eq(ReUser::getUsername, reUserDTO.getUsername()));
        if (reUser != null && reUser.getUsername().equals(reUserDTO.getUsername())) {
            throw new UserValidateException(ReErrorEnum.USERNAME_ALREADY_EXIST);
        }
    }

    /**
     * 用户DTO基础校验，主要用于用户注册和添加用户时的校验
     * @param reUserDTO 参数封装
     */
    public void userDtoBaseValidate(ReUserDTO reUserDTO) {
        // 用户名校验
        if (!UserValidatePatternConstant.USERNAME_VALIDATE_PATTERN.matcher(reUserDTO.getUsername()).matches()) {
            throw new UserValidateException(ReErrorEnum.USERNAME_FORMAT_ERROR);
        }
        // 密码校验
        if (!UserValidatePatternConstant.PASSWORD_VALIDATE_PATTERN.matcher(reUserDTO.getPassword()).matches()) {
            throw new UserValidateException(ReErrorEnum.PASSWORD_FORMAT_ERROR);
        }
        // 邮箱校验
        if (!UserValidatePatternConstant.EMAIL_VALIDATE_PATTERN.matcher(reUserDTO.getEmail()).matches()) {
            throw new UserValidateException(ReErrorEnum.EMAIL_FORMAT_ERROR);
        }
        // 手机校验
        if (!UserValidatePatternConstant.PHONE_VALIDATE_PATTERN.matcher(reUserDTO.getPhone()).matches()) {
            throw new UserValidateException(ReErrorEnum.PHONE_FORMAT_ERROR);
        }
    }

    @Override
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
