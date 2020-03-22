package cn.rootelement.es.service.impl;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.entity.ReBlog;
import cn.rootelement.es.service.IReBlogEsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;


@SpringBootTest
@RunWith(JUnit4.class)
class IReBlogEsServiceImplTest {


    @Autowired
    private IReBlogEsService service;

    @Test
    void save() {
        ReBlog reBlog = new ReBlog();
        reBlog.setId(10088);
        ReBlog save = service.save(reBlog);
        System.out.println(save);
    }

    @Test
    void delete() {
        ReBlog reBlog = new ReBlog();
        reBlog.setId(10001);
        service.delete(reBlog);
        System.out.println(service.count());
    }

    @Test
    void getAll() {
        service.getAll().forEach(System.out::println);
    }

    @Test
    void count() {
        System.out.println(service.count());
    }

    @Test
    void query() {
        Page<ReBlog> query = service.query("git", new PageDTO(1, 10));
        query.forEach(System.out::println);
    }
}