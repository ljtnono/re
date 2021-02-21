package cn.ljtnono.re.service.blog;

import cn.ljtnono.re.mapper.blog.TypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 博客类型模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/2/22 12:59 上午
 */
@Service
public class TypeService {

    @Resource
    private TypeMapper typeMapper;


}
