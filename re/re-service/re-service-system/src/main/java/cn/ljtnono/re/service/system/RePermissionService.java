package cn.ljtnono.re.service.system;

import cn.ljtnono.re.mapper.system.RePermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ljt
 * Date: 2020/8/9 17:06
 * Description:
 */
@Service
@Slf4j
public class RePermissionService {


    @Resource
    private RePermissionMapper rePermissionMapper;

}
