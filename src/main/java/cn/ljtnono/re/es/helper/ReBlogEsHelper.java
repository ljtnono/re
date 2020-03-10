package cn.ljtnono.re.es.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

/**
 * 处理blog索引的服务类
 * @author ljt
 * @date 2020/3/10
 * @version 1.0.1
 */
@Component
public class ReBlogEsHelper {

    private ElasticsearchRestTemplate template;

    @Autowired
    public ReBlogEsHelper(ElasticsearchRestTemplate template) {
        this.template = template;
    }

    public void searchByTitle() {

    }

}
