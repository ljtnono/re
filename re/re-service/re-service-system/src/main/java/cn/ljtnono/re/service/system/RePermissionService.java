package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.ReErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.entity.system.RePermission;
import cn.ljtnono.re.entity.system.ReRole;
import cn.ljtnono.re.entity.system.ReRolePermission;
import cn.ljtnono.re.mapper.system.RePermissionMapper;
import cn.ljtnono.re.mapper.system.ReRolePermissionMapper;
import cn.ljtnono.re.vo.system.ReUserLoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ling, Jiatong
 * Date: 2020/8/9 17:06
 * Description: 权限Service类
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
public class RePermissionService {

    @Resource
    private RePermissionMapper rePermissionMapper;
    @Resource
    private ReRolePermissionService reRolePermissionService;

    //*********************************** 接口方法 ***********************************//

    //*********************************** 私有方法 ***********************************//

    //*********************************** 公有方法 ***********************************//

    /**
     * @param idList 权限id列表
     * @return 全部都存在返回true，有一个不存在返回false
     * @author Ling, Jiatong
     *
     */
    @Transactional(readOnly = true)
    public boolean isPermissionExist(List<Integer> idList) {
        if (!CollectionUtils.isEmpty(idList)) {
            Integer count = rePermissionMapper.selectCount(new LambdaQueryWrapper<RePermission>()
                    .in(RePermission::getId, idList));
            return count == idList.size();
        } else {
            throw new ParamException(ReErrorEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 根据permissionList生成menu树
     *
     * @param roleId 角色id
     * @return List<MenuItem> 菜单树
     */
    @Transactional(readOnly = true)
    public List<ReUserLoginVO.MenuItem> generateMenu(Integer roleId) {
        List<Integer> permissionIdList = reRolePermissionService.getPermissionIdListByRoleId(roleId);
        if (!CollectionUtils.isEmpty(permissionIdList)) {
            List<RePermission> permissionList = rePermissionMapper.selectList(new LambdaQueryWrapper<RePermission>()
                    .in(RePermission::getId, permissionIdList));
            List<ReUserLoginVO.MenuItem> menu = new ArrayList<>();
            if (permissionList != null && permissionList.size() != 0) {
                // 生成菜单树
                for (RePermission permission : permissionList) {
                    ReUserLoginVO.MenuItem menuItem = new ReUserLoginVO.MenuItem();
                    menuItem.setId(permission.getId());
                    menuItem.setTitle(permission.getName());
                    menuItem.setType(permission.getType());
                    menuItem.setParentId(permission.getParentId());
                    menu.add(menuItem);
                }
                Map<Integer, List<ReUserLoginVO.MenuItem>> collect = menu.stream().filter(menuItem -> menuItem.getParentId() != -1)
                        .collect(Collectors.groupingBy(ReUserLoginVO.MenuItem::getParentId));
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
