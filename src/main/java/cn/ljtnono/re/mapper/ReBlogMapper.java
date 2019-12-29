package cn.ljtnono.re.mapper;

import cn.ljtnono.re.entity.ReBlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;


/**
 * 博客mapper层
 * @author ljt
 * @date 2019/12/29
 * @version 1.0.1
 */
public interface ReBlogMapper extends BaseMapper<ReBlog> {


    /**
     * 获取所有博客的浏览量总数
     * @return 所有博客的浏览量总数
     */
    @Select("SELECT SUM(view) FROM re_blog")
    Integer countView();

    /**
     * 获取所有博客的评论总数
     * @return 所有博客的评论总数
     */
    @Select("SELECT SUM(comment) FROM re_blog")
    Integer countComment();
}
