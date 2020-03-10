package cn.ljtnono.re.es.repository;

import cn.ljtnono.re.entity.ReBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 博客查询接口
 * @author ljt
 * @date 2020/3/10
 * @version 1.0.1
 */
@Repository
public interface ReBlogEsRepository extends ElasticsearchRepository<ReBlog, Integer> {

}
