package cn.ljtnono.re.service.system;

import cn.ljtnono.re.mapper.system.LogMapper;
import lombok.Data;

import javax.annotation.Resource;

/**
 * 日志模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/1/31 21:01
 */
@Data
public class LogService {

    @Resource
    private LogMapper logMapper;

}
