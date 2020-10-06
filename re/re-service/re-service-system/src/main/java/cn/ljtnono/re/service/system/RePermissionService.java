package cn.ljtnono.re.service.system;

import cn.ljtnono.re.entity.system.ReRolePermission;
import cn.ljtnono.re.mapper.system.RePermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljt
 * Date: 2020/8/9 17:06
 * Description: 权限Service类
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
public class RePermissionService {

}
