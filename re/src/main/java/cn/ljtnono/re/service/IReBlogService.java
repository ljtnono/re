package cn.ljtnono.re.service;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReBlogSearchDTO;
import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.service.common.IReEntityService;
import cn.ljtnono.re.vo.JsonResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
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
    JsonResultVO listBlogPage(Integer page, Integer count);

    /**
     * 获取首页猜你喜欢
     * @return 首页猜你喜欢博客数据
     */
    JsonResultVO listGuessYouLike();

    /**
     * 分页查询博客列表
     * @param type 类型
     * @param page 页码
     * @param count 每页显示的条数
     * @return JsonResult
     */
    JsonResultVO listBlogPageByType(Integer page, Integer count, final String type);

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

    /**
     * 根据博客标题，博客类型，博客作者模糊查询博客信息
     * @param reBlogSearchDTO 查询条件封装DTO
     * @param pageDTO 分页DTO
     * @return 返回结果对象
     */
    JsonResultVO search(ReBlogSearchDTO reBlogSearchDTO, PageDTO pageDTO);

    /**
     * 根据博客id恢复博客
     * @param id 博客id
     * @return 返回结果对象
     */
    JsonResultVO restore(Serializable id);

    /**
     * 获取首页热门文章列表
     * @return 热门文章列表
     */
    JsonResultVO listHotArticles();
}
