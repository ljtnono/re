package cn.ljtnono.re.controller;

import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.util.BlogIndexUtil;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 处理页面路由的Controller
 * @author ljt
 * @date 2019/12/28
 * @version 1.0.1
 */
@Controller
@ApiOperation("路由信息")
@Slf4j
public class PageController {

    private IReBlogService iReBlogService;

    @Autowired
    public PageController(IReBlogService iReBlogService) {
        this.iReBlogService = iReBlogService;
    }

    @GetMapping("/")
    @ApiOperation(value = "跳转到首页")
    public String fore(ModelMap map) {
        map.addAttribute("currentPage", "index");
        return "fore/index";
    }

    @RequestMapping("/{page:^(?!swagger-ui.html).*$}")
    public String foreTemplates(@PathVariable final String page, final ModelMap map) {
        setActivePage(page, map);
        return "fore/" + page;
    }

    @RequestMapping("/admin/login")
    public String toLogin() {
        return "back/login";
    }

    @RequestMapping({"/admin", "/admin/"})
    public String back(ModelMap map) {
        return "back/index";
    }

    @RequestMapping("/admin/{page}")
    public String backTemplates(@PathVariable String page) {
        return "back/" + page;
    }


    /**
     * 根据路由设置当前页面
     * @param map 存储值对象
     */
    private void setActivePage(final String page, final ModelMap map) {
        switch (page) {
            case "articles":
                map.addAttribute("currentPage", "articles");
                break;
            case "support":
                map.addAttribute("currentPage", "support");
                break;
            case "about":
                map.addAttribute("currentPage", "about");
                break;
            default:
                map.addAttribute("currentPage", "index");
        }
    }

    /**
     * 根据博客的id获取博客内容
     * @param id 博客的id
     * @param modelMap thymeleaf属性集合
     * @return 跳转到article页面
     */
    @GetMapping("/article/{id:\\d+}")
    public String article(@PathVariable final String id, ModelMap modelMap) {
        // 如果参数为空
        if (StringUtil.isEmpty(id)) {
            log.info("博客id不能为空");
            return "forward:/error/404";
        }
        ReBlog byId = iReBlogService.getById(id);
        if (byId == null || byId.getStatus() == 0) {
            // 如果没有查询到或者该博客已经删除，那么返回404页面
            log.info("博客不存在或已经删除");
            return "forward:/error/404";
        }
        // 每访问一次，将该博客的浏览量 + 1
        modelMap.addAttribute("blog", byId);
        ReBlog next = iReBlogService.getById(Integer.parseInt(id) + 1);
        iReBlogService.update(new UpdateWrapper<ReBlog>().eq("id", byId.getId()).set("view", byId.getView() + 1));
        if (next != null) {
            modelMap.addAttribute("next", next);
        }
        ReBlog prev = iReBlogService.getById(Integer.parseInt(id) - 1);
        if (next != null) {
            modelMap.addAttribute("prev", prev);
        }
        modelMap.addAttribute("currentPage", "articles");
        return "fore/article";
    }

    /**
     *@Description: 全文检索博客
     *@Param: page 页数
     *@return: keyWord 搜索关键字
     *@date: 2019/12/30
     */
    @RequestMapping("/search/{page}/{keyWord}")
    public String search(ModelMap modelMap,
                               @PathVariable(value = "keyWord", required = false) String keyWord,
                               @PathVariable(value = "page", required = false) String page,
                               HttpServletRequest request) throws Exception {
        //String rootPath=request.getServletContext().getRealPath("/");
        if (StringUtil.isEmpty(page)){
            page = "1";
        }
        int pageSize = 10;
        modelMap.addAttribute("pageTitle", "搜索关键字'" + keyWord + "'结果页面");
//        modelMap.addAttribute()
        // modelMap.addObject("pageTitle", "搜索关键字'" + q + "'结果页面_java开源博客系统");
        //modelMap.addObject("mainPage", "foreground/blog/result.jsp");
        BlogIndexUtil blogIndexUtil = new BlogIndexUtil();

        List<ReBlog> blogList = blogIndexUtil.searchBlog(keyWord);

        long totalPage = blogList.size() % pageSize == 0 ? 1: blogList.size()  / pageSize + 1;
        if(blogList.size() % pageSize == 0){
            page = String.valueOf (Integer.parseInt(page) - 1);
        }

        //这个toindex就是 截取的start end 的end位置。
        Integer toIndex = blogList.size() >= Integer.parseInt(page) * pageSize ? Integer.parseInt(page) * pageSize
                : blogList.size();
        modelMap.addAttribute("blogList", blogList);
        modelMap.addAttribute("keyWord", keyWord);
        modelMap.addAttribute("page",page);
        //ModelAndView model = new ModelAndView("fore/article");
        return "fore/result";
    }
}
