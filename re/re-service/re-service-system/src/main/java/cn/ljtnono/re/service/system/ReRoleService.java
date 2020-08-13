package cn.ljtnono.re.service.system;

import cn.ljtnono.re.dto.system.ReRoleDTO;
import cn.ljtnono.re.mapper.system.ReRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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


    public void addRole(ReRoleDTO reRoleDTO) {

    }

}
