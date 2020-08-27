package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.enumeration.ReStatusEnum;
import cn.ljtnono.re.common.exception.GlobalException;
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
 * @author ljt
 * Date: 2020/8/9 16:30
 * Description:
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
public class ReRoleService {

    @Resource
    private ReRoleMapper reRoleMapper;


    //*********************************** 增删改查 ***********************************//

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
        setCreateTimeAndModifyTime(reRole);

        // TODO 设置
    }

    private void setCreateTimeAndModifyTime(ReRole reRole) {
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



}
