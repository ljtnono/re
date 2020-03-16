package cn.ljtnono.re.es;

import cn.ljtnono.re.entity.ReBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * elasticsearch操作blog类的dao层
 * @author ljt
 * @date 2020/3/16
 * @version 1.0.1
 */
public interface ReBlogRepository extends ElasticsearchRepository<ReBlog, Integer> {

}
