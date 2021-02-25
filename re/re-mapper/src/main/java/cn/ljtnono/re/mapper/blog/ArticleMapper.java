package cn.ljtnono.re.mapper.blog;

import cn.ljtnono.re.dto.blog.article.ArticleListQueryDTO;
import cn.ljtnono.re.entity.blog.Article;
import cn.ljtnono.re.vo.blog.article.ArticleListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 博客文章模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2021/1/24 15:15
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页获取博客文章列表
     *
     * @param page 分页对象
     * @param dto 博客文章列表查询DTO对象
     * @return 博客文章列表VO对象分页对象
     * @author Ling, Jiatong
     */
    IPage<ArticleListVO> getList(Page<?> page, @Param("dto") ArticleListQueryDTO dto);
}
