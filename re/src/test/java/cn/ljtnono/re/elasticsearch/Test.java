package cn.ljtnono.re.elasticsearch;

import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.vo.JsonResultVO;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private IReBlogService iReBlogService;

    @org.junit.Test
    public void testCreate() {
        JsonResultVO jsonResultVO = iReBlogService.listEntityAll();
        List<ReBlog> data = (List<ReBlog>) jsonResultVO.getData();
        data.forEach(blog -> {
            IndexQuery build = new IndexQueryBuilder()
                    .withId(blog.getId().toString())
                    .withObject(blog)
                    .build();
            elasticsearchRestTemplate.index(build);
        });
    }
}
