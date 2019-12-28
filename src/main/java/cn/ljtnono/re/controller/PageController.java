package cn.ljtnono.re.controller;

import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 处理页面路由的Controller
 * @author ljt
 * @date 2019/12/28
 * @version 1.0.1
 */
@Controller
@ApiOperation("路由信息")
public class PageController {

    private IReBlogService iReBlogService;

    @Autowired
    public PageController(IReBlogService iReBlogService) {
        this.iReBlogService = iReBlogService;
    }

    private Logger logger = LoggerFactory.getLogger(PageController.class);

    @GetMapping("/")
    @ApiOperation(value = "跳转到首页")
    public String fore(ModelMap map) {
        map.addAttribute("currentPage", "index");
        return "fore/index";
    }

    @GetMapping("/{page:^(?!swagger-ui.html).*$}")
    public String foreTemplates(@PathVariable final String page, final ModelMap map) {
        setActivePage(page, map);
        return "fore/" + page;
    }

    @GetMapping("/admin/login")
    public String toLogin() {
        return "back/login";
    }

    @GetMapping({"/admin", "/admin/"})
    public String back(ModelMap map) {
        return "back/index";
    }

    @GetMapping("/admin/{page:^(?!\\*\\.html)}")
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
    @GetMapping("/article/{id}")
    public String article(@PathVariable final String id, ModelMap modelMap) {
        // 如果参数为空
        if (StringUtil.isEmpty(id)) {
            logger.info("博客id不能为空");
            return "forward:/error/404";
        }
        ReBlog byId = iReBlogService.getById(id);
        if (byId == null || byId.getStatus() == 0) {
            // 如果没有查询到或者该博客已经删除，那么返回404页面
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
}
