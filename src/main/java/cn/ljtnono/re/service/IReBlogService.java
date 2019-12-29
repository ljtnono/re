package cn.ljtnono.re.service;

import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.common.IReEntityService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 博客服务接口
 * @author ljt
 * @date 2019/11/16
 * @version 1.0
 */
public interface IReBlogService extends IService<ReBlog>, IReEntityService<ReBlog> {

    /**
     * 无条件分页查询博客信息
     * @param page 页数
     * @param count 每页条数
     * @return 返回分页数据
     */
    JsonResult listBlogPage(Integer page, Integer count);



    /**
     * 获取首页猜你喜欢
     * @return 首页猜你喜欢博客数据
     */
    List<ReBlog> listGuessYouLike();

    /**
     * 分页查询博客列表
     * @param type 类型
     * @param page 页码
     * @param count 每页显示的条数
     * @return JsonResult
     */
    JsonResult listBlogPageByType(Integer page, Integer count, final String type);

    /**
     * 获取所有博客的浏览量总数
     * @return 所有博客的浏览量总数
     */
    Integer countView();

    /**
     * 获获取所有博客的评论总数
     * @return 获取所有博客的评论总数
     */
    Integer countComment();
}
