package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.util.JJWTUtil;
import cn.ljtnono.re.util.StringUtil;
import cn.ljtnono.re.vo.JsonResultVO;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 处理页面路由的Controller
 * @author ljt
 * @date 2019/12/28
 * @version 1.0.1
 */
@Controller
@Api(value = "页面路由Controller")
@Slf4j
public class PageController {

    private IReBlogService iReBlogService;

    private JJWTUtil jjwtUtil;

    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;

    @Autowired
    public PageController(IReBlogService iReBlogService, JJWTUtil jjwtUtil, @Qualifier("reUserDetailService") UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.iReBlogService = iReBlogService;
        this.jjwtUtil = jjwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
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
    public String back() {
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
            case "topic":
                map.addAttribute("currentPage", "topic");
                break;
            case "books":
                map.addAttribute("currentPage", "books");
                break;
            case "apps":
                map.addAttribute("currentPage", "apps");
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
    public String article(@PathVariable(value = "id") final String id, ModelMap modelMap) {
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

    @GetMapping("/articles")
    public String articles(String type, @Validated PageDTO pageDTO, ModelMap modelMap) {
        JsonResultVO jsonResultVO;
        if (StringUtil.isEmpty(type) || "ALL".equals(type)) {
            jsonResultVO = iReBlogService.listBlogPageByType(pageDTO.getPage(), pageDTO.getCount(), null);
            modelMap.addAttribute("type", "ALL");
        } else {
            jsonResultVO = iReBlogService.listBlogPageByType(pageDTO.getPage(), pageDTO.getCount(), type);
            modelMap.addAttribute("type", type);
        }
        modelMap.addAttribute("data", jsonResultVO.getData())
                .addAttribute("request", jsonResultVO.getRequest())
                .addAttribute("status", jsonResultVO.getStatus())
                .addAttribute("totalCount", jsonResultVO.getTotalCount())
                .addAttribute("fields", jsonResultVO.getFields());
        setActivePage("articles", modelMap);
        return "fore/articles";
    }

    @PostMapping("/admin/login")
    @ResponseBody
    public JsonResultVO login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwt = jjwtUtil.generateToken(userDetails);
        JsonResultVO jsonResultVO = JsonResultVO.successForMessage("登陆成功", 200);
        jsonResultVO.addField("token", jwt);
        return jsonResultVO;
    }
}
