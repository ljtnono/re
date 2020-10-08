package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.enumeration.ReStatusEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceAlreadyExistException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.dto.system.ReRoleDTO;
import cn.ljtnono.re.entity.system.ReRole;
import cn.ljtnono.re.mapper.system.ReRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private RePermissionService rePermissionService;
    @Autowired
    private ReRolePermissionService reRolePermissionService;

    //*********************************** 接口方法 ***********************************//

    @Transactional(readOnly = true)
    public List<ReRole> select() {
        return reRoleMapper.selectList(new LambdaQueryWrapper<ReRole>()
                .select(ReRole::getId, ReRole::getName)
                .eq(ReRole::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
    }

    /**
     * 新增角色
     * @param reRoleDTO 参数封装
     */
    public void addRole(ReRoleDTO reRoleDTO) {
        Optional.ofNullable(reRoleDTO)
                .orElseThrow(() -> new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 校验角色名是否重复
        boolean roleAlreadyExist = isExistByName(reRoleDTO.getName());
        if (roleAlreadyExist) {
            throw new ResourceAlreadyExistException(ReErrorEnum.ROLE_ALREADY_EXIST);
        }
        // 插入角色表
        ReRole reRole = new ReRole();
        BeanUtils.copyProperties(reRoleDTO, reRole);
        reRole.setDeleted(ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        setCreateTimeAndModifyTimeNow(reRole);
        int insert = reRoleMapper.insert(reRole);
        if (insert <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 校验角色权限是否都存在
        List<Integer> permissionIdList = reRoleDTO.getPermissionIdList();
        boolean isPermissionExists = rePermissionService.isPermissionExist(permissionIdList);
        if (isPermissionExists) {
            // 插入角色权限表
            reRolePermissionService.insertReRolePermission(reRole.getId(), permissionIdList);
        } else {
            // 权限不存在
            throw new ResourceNotExistException(ReErrorEnum.PERMISSION_NOT_EXIST);
        }
    }

    /**
     * 根据角色id逻辑删除角色
     * @param id 角色id
     * @author Ling, Jiatong
     *
     */
    public void logicDeleteById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.ROLE_ID_NULL_ERROR));
        // 如果存在，那么执行逻辑删除
        boolean exist = isExistById(id);
        if (exist) {
            int update = reRoleMapper.update(null, new LambdaUpdateWrapper<ReRole>()
                    .set(ReRole::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_DELETED)
                    .set(ReRole::getModifyTime, new Date())
                    .eq(ReRole::getId, id));
            if (update <= 0) {
                throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
            }
        }
    }

    /**
     * 修改角色
     * @param reRoleDTO 参数封装
     * @author Ling, Jiatong
     *
     */
    public void updateRole(ReRoleDTO reRoleDTO) {
        Optional.ofNullable(reRoleDTO)
                .orElseThrow(() -> new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR));
        // 检测该角色是否存在
        boolean existById = isExistById(reRoleDTO.getId());
        if (!existById) {
            throw new ResourceNotExistException(ReErrorEnum.ROLE_NOT_EXIST);
        }
        // 检查用户名是否除了本角色之外存在与其他现存角色名重复
        ReRole reRole = reRoleMapper.selectOne(new LambdaQueryWrapper<ReRole>()
                .select(ReRole::getName)
                .eq(ReRole::getName, reRoleDTO.getName())
                .ne(ReRole::getId, reRoleDTO.getId()));
        Optional.ofNullable(reRole)
                .orElseThrow(() -> new ResourceAlreadyExistException(ReErrorEnum.ROLE_ALREADY_EXIST));
        // 插入角色表
        int update = reRoleMapper.update(null, new LambdaUpdateWrapper<ReRole>()
                .set(ReRole::getName, reRoleDTO.getName())
                .set(ReRole::getModifyTime, new Date())
                .eq(ReRole::getId, reRoleDTO.getId()));
        if (update <= 0) {
            throw new DataBaseException(ReErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 插入角色权限表
        List<Integer> permissionIdList = reRoleDTO.getPermissionIdList();
        boolean isPermissionExists = rePermissionService.isPermissionExist(permissionIdList);
        if (isPermissionExists) {
            // 先删除角色原来的权限
            reRolePermissionService.deleteReRolePermissionByRoleId(reRoleDTO.getId());
            // 插入新的角色权限关系
            reRolePermissionService.insertReRolePermission(reRoleDTO.getId(), permissionIdList);
        } else {
            // 权限不存在
            throw new ResourceNotExistException(ReErrorEnum.PERMISSION_NOT_EXIST);
        }
    }

    /**
     * 分页获取角色列表
     * @param reRoleDTO 参数封装
     * @return IPage<ReRole>
     * @author Ling, Jiatong
     *
     */
    @Transactional(readOnly = true)
    public IPage<ReRole> getRoleListPage(ReRoleDTO reRoleDTO) {
        Optional.ofNullable(reRoleDTO)
                .orElseThrow(() -> new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR));
        Page<ReRole> page = new Page<>(reRoleDTO.getPageNum(), reRoleDTO.getPageSize());
        // 这里只根据角色进行模糊匹配
        LambdaQueryWrapper<ReRole> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(!StringUtils.isEmpty(reRoleDTO.getName()), ReRole::getName, reRoleDTO.getName())
                .eq(ReRole::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED);
        return reRoleMapper.selectPage(page, wrapper);
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
     * 根据角色名判断角色是否存在
     * @param name 角色名
     * @return 已经存在返回true，不存在返回false
     */
    @Transactional(readOnly = true)
    public boolean isExistByName(final String name) {
        // 检查名字
        if (StringUtils.isEmpty(name)) {
            throw new ParamException(ReErrorEnum.ROLE_NAME_EMPTY_ERROR);
        }
        ReRole reRole = reRoleMapper.selectOne(new LambdaQueryWrapper<ReRole>()
                .select(ReRole::getName)
                .eq(ReRole::getName, name)
                .eq(ReRole::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
        return reRole != null;
    }

    /**
     * 根据id检查该角色是否存在
     * @param id 角色id
     * @return 存在返回true，不存在返回false
     * @author Ling, Jiatong
     *
     */
    @Transactional(readOnly = true)
    public boolean isExistById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.ROLE_ID_NULL_ERROR));
        ReRole reRole = reRoleMapper.selectOne(new LambdaQueryWrapper<ReRole>()
                .eq(ReRole::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
        return reRole != null;
    }

    /**
     * 根据id获取角色相关信息
     * @param id 角色id
     * @return ReRole 角色对象
     * @author Ling, Jiatong
     *
     */
    @Transactional(readOnly = true)
    public ReRole getRoleById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(ReErrorEnum.ROLE_ID_NULL_ERROR));
        return reRoleMapper.selectOne(new LambdaQueryWrapper<ReRole>()
                .eq(ReRole::getDeleted, ReStatusEnum.ENTITY_IS_DELETED_NOT_DELETED));
    }

    //*********************************** 其他方法 ***********************************//
}
