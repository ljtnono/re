package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.EntityConstantEnum;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceAlreadyExistException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.dto.system.role.RoleDTO;
import cn.ljtnono.re.dto.system.role.RoleListQueryDTO;
import cn.ljtnono.re.entity.system.Role;
import cn.ljtnono.re.mapper.system.RoleMapper;
import cn.ljtnono.re.vo.system.role.RoleListVO;
import cn.ljtnono.re.vo.system.role.RoleVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色模块service层
 *
 * @author Ling, Jiatong
 * Date: 2020/8/9 16:30
 */
@Slf4j
@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;
    private final PermissionService permissionService;
    private final RolePermissionService rolePermissionService;

    public RoleService(PermissionService permissionService, RolePermissionService rolePermissionService) {
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
    }

    //*********************************** 接口方法 ***********************************//

    /**
     * 获取角色下拉列表
     *
     * @return 角色VO列表数据
     * @author Ling, Jiatong
     */
    public List<RoleVO> select() {
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getName)
                .eq(Role::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        if (!CollectionUtils.isEmpty(roleList)) {
            return roleList.parallelStream()
                    .map(role -> {
                        RoleVO roleVO = new RoleVO();
                        BeanUtils.copyProperties(role, roleVO);
                        return roleVO;
                    }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 新增角色
     *
     * @param dto 角色DTO对象
     * @throws ResourceAlreadyExistException 当角色名与现有角色重复时，会抛出此异常
     * @throws ResourceNotExistException 当角色的权限id列表存在错误值时，会抛出此异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleDTO dto) {
        // 校验角色名是否重复
        boolean roleAlreadyExist = isExistByName(dto.getName());
        if (roleAlreadyExist) {
            throw new ResourceAlreadyExistException(GlobalErrorEnum.ROLE_ALREADY_EXIST);
        }
        // 插入角色表
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        role.setDeleted(EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        setCreateTimeAndModifyTimeNow(role);
        int insert = roleMapper.insert(role);
        if (insert <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 校验角色权限是否都存在
        List<Integer> permissionIdList = dto.getPermissionIdList();
        boolean isPermissionExists = permissionService.isPermissionExist(permissionIdList);
        if (isPermissionExists) {
            // 插入角色权限表
            rolePermissionService.insertRolePermission(role.getId(), permissionIdList);
        } else {
            // 权限不存在
            throw new ResourceNotExistException(GlobalErrorEnum.PERMISSION_NOT_EXIST);
        }
    }

    /**
     * 根据角色id列表逻辑删除角色，当角色不存在时，会抛出异常信息
     *
     * @param dto 角色DTO对象
     * @throws ResourceNotExistException 当角色不存在时抛出此异常
     * @author Ling, Jiatong
     */
    public void logicDelete(RoleDTO dto) {
        List<Integer> idList = dto.getIdList();
        if (!CollectionUtils.isEmpty(idList)) {
            // 校验角色是否存在
            idList.parallelStream()
                    .forEach(this::checkExist);
            roleMapper.update(null, new LambdaUpdateWrapper<Role>()
                    .set(Role::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_DELETED.getValue())
                    .set(Role::getModifyTime, new Date())
                    .in(Role::getId, idList));
        }
    }

    /**
     * 修改角色
     *
     * @param dto 参数封装
     * @author Ling, Jiatong
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleDTO dto) {
        // 检测该角色是否存在
        Role check = isExistById(dto.getId());
        if (check == null) {
            throw new ResourceNotExistException(GlobalErrorEnum.ROLE_NOT_EXIST);
        }
        // 检查用户名是否除了本角色之外存在与其他现存角色名重复
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getName)
                .eq(Role::getName, dto.getName())
                .ne(Role::getId, dto.getId()));
        Optional.ofNullable(role)
                .orElseThrow(() -> new ResourceAlreadyExistException(GlobalErrorEnum.ROLE_ALREADY_EXIST));
        // 插入角色表
        int update = roleMapper.update(null, new LambdaUpdateWrapper<Role>()
                .set(Role::getName, dto.getName())
                .set(Role::getModifyTime, new Date())
                .eq(Role::getId, dto.getId()));
        if (update <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 插入角色权限表
        List<Integer> permissionIdList = dto.getPermissionIdList();
        boolean isPermissionExists = permissionService.isPermissionExist(permissionIdList);
        if (isPermissionExists) {
            // 先删除角色原来的权限
            rolePermissionService.deleteRolePermissionByRoleId(dto.getId());
            // 插入新的角色权限关系
            rolePermissionService.insertRolePermission(dto.getId(), permissionIdList);
        } else {
            // 权限不存在
            throw new ResourceNotExistException(GlobalErrorEnum.PERMISSION_NOT_EXIST);
        }
    }

    /**
     * 分页获取角色列表
     *
     * @param dto 角色列表查询DTO对象
     * @return 角色列表VO对象列表
     * @author Ling, Jiatong
     */
    public IPage<RoleListVO> getList(RoleListQueryDTO dto) {
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        try {
            dto.generateSortCondition();
        } catch (IllegalArgumentException e) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        // TODO 待完成
        return roleMapper.getList(page, dto);
    }

    //*********************************** 私有方法 ***********************************//

    /**
     * 设置创建时间和最后修改时间为当前时间
     *
     * @param role 角色对象
     * @author Ling, Jiatong
     */
    private void setCreateTimeAndModifyTimeNow(Role role) {
        Date now = new Date();
        role.setCreateTime(now);
        role.setModifyTime(now);
    }

    //*********************************** 公共方法 ***********************************//

    /**
     * 根据用户id获取角色id和角色名
     * @param userId 用户id
     * @return ReRole 角色对象
     * @author Ling, Jiatong
     */
    public Role getRoleIdAndNameByUserId(Integer userId) {
        Optional.ofNullable(userId)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.USER_ID_NULL_ERROR));
        return roleMapper.getRoleIdAndNameByUserId(userId);
    }

    /**
     * 根据角色名判断角色是否存在
     *
     * @param name 角色名
     * @return 已经存在返回true，不存在返回false
     */
    public boolean isExistByName(final String name) {
        // 检查名字
        if (StringUtils.isEmpty(name)) {
            throw new ParamException(GlobalErrorEnum.ROLE_NAME_EMPTY_ERROR);
        }
        Role reRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getName)
                .eq(Role::getName, name)
                .eq(Role::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        return reRole != null;
    }

    /**
     * 根据id检查该角色是否存在
     *
     * @param id 角色id
     * @return 存在返回该角色实体对象，不存在返回{@literal null}
     * @author Ling, Jiatong
     */
    public Role isExistById(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.ROLE_ID_NULL_ERROR));
        return roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getId, id)
                .eq(Role::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
    }

    /**
     * 检查角色是否存在，如果不存在那么抛出异常
     *
     * @param id 角色id
     * @throws ResourceNotExistException 当角色不存在时，抛出此异常
     * @author Ling, Jiatong
     */
    public void checkExist(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.ROLE_ID_NULL_ERROR));
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getId, id)
                .eq(Role::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        Optional.ofNullable(role)
                .orElseThrow(() -> new ResourceNotExistException(GlobalErrorEnum.ROLE_NOT_EXIST));
    }

    /**
     * 根据id获取角色相关信息
     *
     * @param id 角色id
     * @return 角色VO对象
     * @author Ling, Jiatong
     */
    public RoleVO getRoleById(Integer id) {
        Role role = isExistById(id);
        if (role == null) {
            throw new ResourceNotExistException(GlobalErrorEnum.ROLE_NOT_EXIST);
        }
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);
        return roleVO;
    }

    //*********************************** 其他方法 ***********************************//
}
