package cn.rootelement.es.repository;

import cn.rootelement.entity.ReBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * elasticsearch操作blog类的dao层
 * @author ljt
 * @date 2020/3/16
 * @version 1.0.1
 */
@Repository
public interface ReBlogEsRepository extends ElasticsearchRepository<ReBlog, Integer> {

    /**
     * 根据blog的以下字段来查询符合分词的内容
     * @param title 博客标题
     * @param author 博客作者
     * @param summary 博客简介
     * @param contentMarkdown 博客内容
     * @param pageable 分页对象
     * @return 分页对象
     */
    Page<ReBlog> findReBlogsByTitleOrAuthorOrSummaryOrContentMarkdown(String title, String author, String summary, String contentMarkdown, Pageable pageable);
}
