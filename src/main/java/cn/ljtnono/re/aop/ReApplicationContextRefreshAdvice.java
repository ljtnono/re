package cn.ljtnono.re.aop;

import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.entity.ReLink;
import cn.ljtnono.re.service.IReBlogService;
import cn.ljtnono.re.service.IReBlogTypeService;
import cn.ljtnono.re.service.IReLinkService;
import cn.ljtnono.re.util.SpringBeanUtil;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.Collection;
import java.util.List;

/**
 * 刷新servletContext域的通知
 * @author ljt
 * @date 2020/2/24
 * @version 1.0.1
 */
@Aspect
@Component
public class ReApplicationContextRefreshAdvice {

    private SpringBeanUtil springBeanUtil;

    private IReBlogTypeService iReBlogTypeService;

    private IReLinkService iReLinkService;

    private IReBlogService iReBlogService;

    public ReApplicationContextRefreshAdvice(SpringBeanUtil springBeanUtil, IReBlogTypeService iReBlogTypeService, IReLinkService iReLinkService, IReBlogService iReBlogService) {
        this.springBeanUtil = springBeanUtil;
        this.iReBlogTypeService = iReBlogTypeService;
        this.iReLinkService = iReLinkService;
        this.iReBlogService = iReBlogService;
    }

    @Pointcut("execution(public * cn.ljtnono.re.controller.ReBlogController.save*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReBlogController.update*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReBlogController.delete*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReBlogController.restore*(..))")
    public void refreshGuessYouLikePoint() {}

    @Pointcut("execution(public * cn.ljtnono.re.controller.ReBlogTypeController.save*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReBlogTypeController.update*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReBlogTypeController.delete*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReBlogTypeController.restore*(..))")
    public void refreshBlogTypeListPoint() {}

    @Pointcut("execution(public * cn.ljtnono.re.controller.ReLinkController.save*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReLinkController.update*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReLinkController.delete*(..)) || " +
            "execution(public * cn.ljtnono.re.controller.ReLinkController.restore*(..))")
    public void refreshListOutLinkAllPoint() {}

    @AfterReturning(pointcut = "refreshBlogTypeListPoint()")
    public void refreshBlogTypeList() {
        ServletContext servletContext = springBeanUtil.getServletContext();
        Collection<?> listBlogTypeResult = iReBlogTypeService.listEntityAll().getData();
        servletContext.setAttribute("blogTypeList", listBlogTypeResult);
    }

    @AfterReturning(pointcut = "refreshListOutLinkAllPoint()")
    public void refreshListOutLinkAll() {
        ServletContext servletContext = springBeanUtil.getServletContext();
        List<ReLink> listBlogTypeResult = iReLinkService.listOutLinkAll();
        servletContext.setAttribute("linkList", listBlogTypeResult);
    }

    @AfterReturning(pointcut = "refreshGuessYouLikePoint()")
    public void refreshGuessYouLike() {
        ServletContext servletContext = springBeanUtil.getServletContext();
        List<ReBlog> listGuessYouLike = iReBlogService.listGuessYouLike();
        servletContext.setAttribute("guessYouLikeList", listGuessYouLike);
    }
}
