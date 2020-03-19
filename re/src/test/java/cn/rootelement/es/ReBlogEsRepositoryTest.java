package cn.rootelement.es;

import cn.rootelement.entity.ReBlog;
import cn.rootelement.es.repository.ReBlogEsRepository;
import cn.rootelement.service.IReBlogService;
import cn.rootelement.vo.JsonResultVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Iterator;
import java.util.List;

@SpringBootTest
@RunWith(JUnit4.class)
class ReBlogEsRepositoryTest {

    @Autowired
    private ReBlogEsRepository reBlogEsRepository;

    @Autowired
    private IReBlogService iReBlogService;

    @Test
    void putData() {
        JsonResultVO resultVO = iReBlogService.listEntityAll();
        List<ReBlog> blogList = (List<ReBlog>) resultVO.getData();
        blogList.forEach(reBlog -> reBlogEsRepository.save(reBlog));
    }


    @Test
    void findReBlogsByTitleAndAuthorAndContentMarkdown() {
        Page<ReBlog> github = reBlogEsRepository.findReBlogsByTitleOrAuthorOrSummaryOrContentMarkdown("github","github", "github", "github", PageRequest.of(0, 10));
        Iterator<ReBlog> iterator = github.iterator();
        while (iterator.hasNext()) {
            ReBlog next = iterator.next();
            System.out.println(next.getId() + "  " + next.getTitle());
        }
    }
}