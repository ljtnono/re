package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.constant.UserValidatePatternConstant;
import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.exception.UserValidateException;
import cn.ljtnono.re.dto.system.ReUserDTO;
import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReRole;
import cn.ljtnono.re.entity.system.ReUser;
import cn.ljtnono.re.mapper.system.ReUserMapper;
import cn.ljtnono.re.common.vo.ReJsonResultVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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


    /**
     * 用户注册
     * @param reUserDTO 用户参数封装
     * @return ReJsonResultVO<?>
     */
    public ReJsonResultVO<?> register(ReUserDTO reUserDTO) {
        Optional.ofNullable(reUserDTO)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 基础校验
        userDtoBaseValidate(reUserDTO);
        // 用户名重复校验
        usernameDuplicateValidate(reUserDTO);
        // TODO 验证码校验
        verifyCodeValidate(reUserDTO);

        // TODO 构建用户实体和角色实体，插入到相应的表中去
        ReUser reUser = new ReUser();
        BeanUtils.copyProperties(reUserDTO, reUser);
        reUser.setDeleted(0);
        setCreateTimeAndModifyTime(reUser);
        int insert = reUserMapper.insert(reUser);
        if (insert <= 0) {
            throw new GlobalException(ReErrorEnum.SYSTEM_ERROR);
        }
        return ReJsonResultVO.success(null);
    }

    /**
     * 验证码校验
     * @param reUserDTO 参数封装
     */
    public void verifyCodeValidate(ReUserDTO reUserDTO) {

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
        if (UserValidatePatternConstant.USERNAME_VALIDATE_PATTERN.matcher(reUserDTO.getUsername()).matches()) {

        }
        // 密码校验
        if (UserValidatePatternConstant.PASSWORD_VALIDATE_PATTERN.matcher(reUserDTO.getPassword()).matches()) {

        }
        // 邮箱校验
        if (UserValidatePatternConstant.EMAIL_VALIDATE_PATTERN.matcher(reUserDTO.getEmail()).matches()) {

        }
        // 手机校验
        if (UserValidatePatternConstant.PHONE_VALIDATE_PATTERN.matcher(reUserDTO.getPhone()).matches()) {

        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws GlobalException {
        // 根据用户名查出用户所有权限
        ReUserDTO reUserDTO = new ReUserDTO();
        reUserDTO.setUsername(s);
        ReUser reUser = reUserMapper.selectOne(new QueryWrapper<ReUser>().lambda()
                // 获取全部字段
                .select(ReUser.class, tableFieldInfo -> true)
                .eq(ReUser::getDeleted, 0));
        // 用户不存在
        Optional.ofNullable(reUser)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.USER_NOT_EXIST));
        List<RePermission> permission = reUserMapper.getPermissionExpressionListByUserId(reUserDTO.getId());
        reUser.setAuthorities(permission);
        return reUser;
    }
}
