package cn.ljtnono.re.listener;

import cn.ljtnono.re.entity.*;
import cn.ljtnono.re.service.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * 容器启动监听器
 *
 * @author ljt
 * @version 1.0.2
 * @date 2019/10/30
 */
@Component
@Slf4j
public class ReApplicationContextListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private IReBlogService iReBlogService;

    private IReLinkService iReLinkService;

    private IReBlogTypeService iReBlogTypeService;

    private IReConfigService iReConfigService;

    private IReImageService iReImageService;

    private ApplicationContext applicationContext;

    @Autowired
    public ReApplicationContextListener(IReBlogService iReBlogService, IReLinkService iReLinkService, IReBlogTypeService iReBlogTypeService, IReConfigService iReConfigService, IReImageService iReImageService) {
        this.iReBlogService = iReBlogService;
        this.iReLinkService = iReLinkService;
        this.iReBlogTypeService = iReBlogTypeService;
        this.iReConfigService = iReConfigService;
        this.iReImageService = iReImageService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ServletContext servletContext = applicationContext.getBean(ServletContext.class);
        // 设置相关系统信息
        setSysConfigInfo();
        // 加载初始化数据
        loadApplicationContextAttribute(servletContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 从数据库中加载初始化需要的内容
     * @param servletContext application实例
     */
    private void loadApplicationContextAttribute(ServletContext servletContext) {
        List<ReBlogType> listBlogTypeResult = (List<ReBlogType>) iReBlogTypeService.listEntityAll().getData();
        // 获取首页外部链接
        List<ReLink> listLinkResult = iReLinkService.listOutLinkAll();
        // 获取所有的配置项
        List<ReConfig> list = iReConfigService.list();
        // 获取首页猜你喜欢
        List<ReBlog> listGuessYouLike = iReBlogService.listGuessYouLike();
        ReImage qrCodeWeChat = iReImageService.getQrCodeWeChat();
        ReImage avatar = iReImageService.getAvatar();
        ReImage qrCodeWeChatSk = iReImageService.getQrCodeWeChatSk();
        ReImage qrCodeZfb = iReImageService.getQrCodeZfb();
        // 将每一个配置项设置到application域
        list.forEach(reConfig -> servletContext.setAttribute(reConfig.getKey(), reConfig.getValue()));
        // 获取总的文章数
        int blogTotalCount = iReBlogService.count();
        log.info("文章总数：" + blogTotalCount);
        int blogViewTotalCount = iReBlogService.countView();
        log.info("文章浏览量总数：" + blogViewTotalCount);
        int blogTypeTotalCount = iReBlogTypeService.count();
        log.info("文章类型总数：" + blogTypeTotalCount);
        int blogCommentTotalCount = iReBlogService.countComment();
        log.info("文章评论总数：" + blogCommentTotalCount);
        servletContext.setAttribute("guessYouLikeList", listGuessYouLike);
        servletContext.setAttribute("blogTypeList", listBlogTypeResult);
        servletContext.setAttribute("linkList", listLinkResult);
        servletContext.setAttribute("qrCodeWeChat", qrCodeWeChat);
        servletContext.setAttribute("avatar", avatar);
        servletContext.setAttribute("qrCodeWeChatSk", qrCodeWeChatSk);
        servletContext.setAttribute("qrCodeZfb", qrCodeZfb);
        servletContext.setAttribute("blogTotalCount", blogTotalCount);
        servletContext.setAttribute("blogViewTotalCount", blogViewTotalCount);
        servletContext.setAttribute("blogTypeTotalCount", blogTypeTotalCount);
        servletContext.setAttribute("blogCommentTotalCount", blogCommentTotalCount);
    }

    /**
     * 获取系统相关信息，并将信息存储到数据库中
     */
    private void setSysConfigInfo() {
        // 获取系统信息
        String javaVersion = System.getProperty("java.version");
        log.info("java.version = " + javaVersion);
        String osName = System.getProperty("os.name");
        log.info("os.name = " + osName);
        iReConfigService.update(new UpdateWrapper<ReConfig>().set("`value`", javaVersion).eq("`key`", "java_version"));
        iReConfigService.update(new UpdateWrapper<ReConfig>().set("`value`", osName).eq("`key`", "os_name"));
    }
}
