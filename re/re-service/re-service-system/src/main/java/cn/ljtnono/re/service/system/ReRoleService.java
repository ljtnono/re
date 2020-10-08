package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.enumeration.ReStatusEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.dto.system.ReRoleDTO;
import cn.ljtnono.re.entity.system.ReRole;
import cn.ljtnono.re.mapper.system.ReRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 16:30
 * Description: 角色Service类
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
public class ReRoleService {

    @Resource
    private ReRoleMapper reRoleMapper;


    //*********************************** 接口方法 ***********************************//

    /**
     * 新增角色
     * @param reRoleDTO 参数封装
     */
    public void addRole(ReRoleDTO reRoleDTO) {
        Optional.ofNullable(reRoleDTO)
                .orElseThrow(() -> new GlobalException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 检查名字
        if (StringUtils.isEmpty(reRoleDTO.getName())) {
            throw new GlobalException(ReErrorEnum.ROLE_NAME_EMPTY_ERROR);
        }
        roleNameDuplicateCheck(reRoleDTO.getName());
        ReRole reRole = new ReRole();
        BeanUtils.copyProperties(reRoleDTO, reRole);
        reRole.setDeleted(ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        setCreateTimeAndModifyTimeNow(reRole);

        // TODO 设置
    }

    //*********************************** 私有方法 ***********************************//

    /**
     * @param reRole 角色对象
     * @author Ling, Jiatong
     *
     */
    private void setCreateTimeAndModifyTimeNow(ReRole reRole) {
        Date now = new Date();
        reRole.setCreateTime(now);
        reRole.setModifyTime(now);
    }

    /**
     * 角色名重复性校验
     * @param name 角色名
     */
    private void roleNameDuplicateCheck(String name) {
        ReRole reRole = reRoleMapper.selectOne(new LambdaQueryWrapper<ReRole>()
                .select(ReRole::getName)
                .eq(ReRole::getName, name));
        if (reRole != null) {
            throw new GlobalException(ReErrorEnum.ROLE_ALREADY_EXIST);
        }
    }

    //*********************************** 公共方法 ***********************************//

    /**
     * 根据用户id获取角色id和角色名
     * @param userId 用户id
     * @return ReRole 角色对象
     * @author Ling, Jiatong
     *
     */
    @Transactional(readOnly = true)
    public ReRole getRoleIdAndNameByUserId(Integer userId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new ParamException(ReErrorEnum.USER_ID_NULL_ERROR));
        return reRoleMapper.getRoleIdAndNameByUserId(userId);
    }

    /**
     * 根据id检查该角色是否存在
     * @param id 角色id
     * @return 存在返回true，不存在返回false
     * @author Ling, Jiatong
     *
     */
    public boolean isExistById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.ROLE_ID_NULL_ERROR));
        ReRole reRole = reRoleMapper.selectOne(new LambdaQueryWrapper<ReRole>()
                .eq(ReRole::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
        return reRole != null;
    }

    //*********************************** 其他方法 ***********************************//
}
