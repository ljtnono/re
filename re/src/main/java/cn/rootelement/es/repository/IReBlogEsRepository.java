package cn.rootelement.es.repository;

import cn.rootelement.entity.ReBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * elasticsearch操作blog类的dao层
 * @author ljt
 * @date 2020/3/16
 * @version 1.0.1
 */
@Repository
public interface IReBlogEsRepository extends ElasticsearchRepository<ReBlog, Integer> {

}
