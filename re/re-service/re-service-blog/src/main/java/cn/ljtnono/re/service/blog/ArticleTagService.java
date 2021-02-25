package cn.ljtnono.re.service.blog;

import cn.ljtnono.re.entity.blog.ArticleTag;
import cn.ljtnono.re.mapper.blog.ArticleTagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章标签关联模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/2/24 1:28 上午
 */
@Service
public class ArticleTagService {

    @Resource
    private ArticleTagMapper articleTagMapper;

    //*********************************** 接口调用 ***********************************//


    //*********************************** 私有函数 ***********************************//

    //*********************************** 公用函数 ***********************************//

    /**
     * 插入文章标签关联表
     * 不校验参数，谨慎使用
     *
     * @param articleTag 文章标签关联表实体
     * @author Ling, Jiatong
     */
    public void insertArticleTag(ArticleTag articleTag) {
        articleTagMapper.insert(articleTag);
    }

    /**
     * 删除文章所有的标签
     * 不校验参数，谨慎使用
     *
     * @param articleId 文章id
     * @author Ling, Jiatong
     */
    public void deleteArticleTagByArticleId(Integer articleId) {
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
    }


    /**
     * 删除文章所有的标签
     * 不校验参数，谨慎使用
     *
     * @param articleIdList 文章id列表
     * @author Ling, Jiatong
     */
    public void deleteArticleTagByArticleIdList(List<Integer> articleIdList) {
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getArticleId, articleIdList));
    }

    /**
     * 根据博客文章id获取标签id列表
     * 不存在则返回空列表
     *
     * @param articleId 博客文章id
     * @return 博客标签id列表
     * @author Ling, Jiatong
     */
    public List<Integer> getTagIdListByArticleId(Integer articleId) {
        List<ArticleTag> articleTagList = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>()
                .select(ArticleTag::getTagId)
                .eq(ArticleTag::getArticleId, articleId));
        if (CollectionUtils.isEmpty(articleTagList)) {
            return List.of();
        }
        return articleTagList
                .stream()
                .map(ArticleTag::getTagId)
                .distinct()
                .collect(Collectors.toList());
    }

    //*********************************** 其他函数 ***********************************//


}
