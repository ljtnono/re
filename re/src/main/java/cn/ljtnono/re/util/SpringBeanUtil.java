package cn.ljtnono.re.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * spring bean 工具
 * @author ljt
 * @date 2020/2/20
 * @version 1.0.1
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    /**
     * 获取applicationContext
     * @return applicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name 要获取的Bean的名字
     * @return bean
     */
    public Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @param clazz 要获取的对象的class对象
     * @param <T> 泛型
     * @return 该对象
     */
    public <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name 对象名
     * @param clazz 对象class对象
     * @param <T> 泛型
     * @return 该对象
     */
    public <T> T getBean(String name,Class<T> clazz){
        Assert.hasText(name,"name为空");
        return getApplicationContext().getBean(name, clazz);
    }

    public ServletContext getServletContext() {
        WebApplicationContext webApplicationContext = (WebApplicationContext) getApplicationContext();
        assert webApplicationContext != null;
        return webApplicationContext.getServletContext();
    }
}
