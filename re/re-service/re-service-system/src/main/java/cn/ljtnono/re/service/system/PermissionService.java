package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.entity.system.Permission;
import cn.ljtnono.re.mapper.system.PermissionMapper;
import cn.ljtnono.re.vo.system.user.UserLoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限模块service层
 *
 * @author Ling, Jiatong
 * Date: 2020/8/9 17:06
 */
@Slf4j
@Service
public class PermissionService {

    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RolePermissionService rolePermissionService;

    //*********************************** 接口方法 ***********************************//

    //*********************************** 私有方法 ***********************************//

    //*********************************** 公有方法 ***********************************//

    /**
     * @param idList 权限id列表
     * @return 全部都存在返回true，有一个不存在返回false
     * @author Ling, Jiatong
     *
     */
    public boolean isPermissionExist(List<Integer> idList) {
        if (!CollectionUtils.isEmpty(idList)) {
            Integer count = permissionMapper.selectCount(new LambdaQueryWrapper<Permission>()
                    .in(Permission::getId, idList));
            return count == idList.size();
        } else {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 根据permissionList生成menu树
     *
     * @param roleId 角色id
     * @return List<MenuItem> 菜单树
     */
    public List<UserLoginVO.MenuItem> generateMenu(Integer roleId) {
        List<Integer> permissionIdList = rolePermissionService.getPermissionIdListByRoleId(roleId);
        if (!CollectionUtils.isEmpty(permissionIdList)) {
            List<Permission> permissionList = permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                    .in(Permission::getId, permissionIdList));
            List<UserLoginVO.MenuItem> menu = new ArrayList<>();
            if (permissionList != null && permissionList.size() != 0) {
                // 生成菜单树
                for (Permission permission : permissionList) {
                    UserLoginVO.MenuItem menuItem = new UserLoginVO.MenuItem();
                    menuItem.setId(permission.getId());
                    menuItem.setTitle(permission.getName());
                    menuItem.setType(permission.getType());
                    menuItem.setParentId(permission.getParentId());
                    menu.add(menuItem);
                }
                Map<Integer, List<UserLoginVO.MenuItem>> collect = menu.stream().filter(menuItem -> menuItem.getParentId() != -1)
                        .collect(Collectors.groupingBy(UserLoginVO.MenuItem::getParentId));
                menu.forEach(menuItem -> menuItem.setSub(collect.get(menuItem.getId())));
                return menu.stream().filter(menuItem -> menuItem.getParentId().equals(-1)).collect(Collectors.toList());
            }
            return menu;
        } else {
            return Lists.newArrayList();
        }
    }

    //*********************************** 其他方法 ***********************************//


}
