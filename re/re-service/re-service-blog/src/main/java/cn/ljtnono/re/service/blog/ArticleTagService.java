package cn.ljtnono.re.service.blog;

import cn.ljtnono.re.mapper.blog.ArticleTagMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 文章标签关联模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/2/24 1:28 上午
 */
@Service
public class ArticleTagService {

    @Resource
    private ArticleTagMapper articleTagMapper;




}
