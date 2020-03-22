package cn.rootelement.es.service.impl;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.entity.ReBlog;
import cn.rootelement.es.repository.IReBlogEsRepository;
import cn.rootelement.es.service.IReBlogEsService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;


/**
 * 处理ReBlog es记录的service类
 * @author ljt
 * @date 2020/3/21
 * @version 1.0.1
 */
@Service
public class IReBlogEsServiceImpl implements IReBlogEsService {

    private IReBlogEsRepository repository;


    public IReBlogEsServiceImpl(IReBlogEsRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReBlog save(ReBlog reBlog) {
        return repository.save(reBlog);
    }

    @Override
    public void delete(ReBlog reBlog) {
        repository.delete(reBlog);
    }

    @Override
    public Iterable<ReBlog> getAll() {
        return repository.findAll();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Page<ReBlog> query(String condition, PageDTO pageDTO) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(condition, "title", "author", "contentMarkdown", "summary"))
                .withPageable(PageRequest.of(pageDTO.getPage() - 1, pageDTO.getCount()))
                .build();
        return repository.search(searchQuery);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
