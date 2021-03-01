package cn.ljtnono.re.service.blog;

import cn.ljtnono.re.common.constant.blog.BlogConstant;
import cn.ljtnono.re.common.constant.system.UserConstant;
import cn.ljtnono.re.common.enumeration.EntityConstantEnum;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.enumeration.system.ConfigKeyEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.dto.blog.article.*;
import cn.ljtnono.re.dto.blog.tag.TagAddDTO;
import cn.ljtnono.re.entity.blog.Article;
import cn.ljtnono.re.entity.blog.ArticleTag;
import cn.ljtnono.re.entity.blog.Tag;
import cn.ljtnono.re.mapper.blog.ArticleMapper;
import cn.ljtnono.re.service.system.ConfigService;
import cn.ljtnono.re.service.system.UserService;
import cn.ljtnono.re.vo.blog.article.ArticleDetailVO;
import cn.ljtnono.re.vo.blog.article.ArticleListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 博客文章模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/1/24 15:14
 */
@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private TagService tagService;
    //*********************************** 接口调用 ***********************************//

    /**
     * 新增博客
     *
     * @param dto 新增博客
     * @author Ling, Jiatong
     */
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(ArticleAddDTO dto) {
        // 校验参数
        checkArticleAddAndUpdateDTO(dto);
        // 校验用户是否存在
        if (!UserConstant.ANONYMOUS_USER_ID.equals(dto.getUserId())) {
            userService.checkExist(dto.getUserId());
        }
        Article article = new Article();
        article.setDeleted(EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        article.setDraft(BlogConstant.BLOG_NOT_DRAFT);
        article.setCreateTime(new Date());
        article.setModifyTime(new Date());
        article.setView(0);
        article.setFavorite(0);
        article.setRecommend(dto.getRecommend());
        article.setUserId(dto.getUserId());
        article.setTitle(dto.getTitle());
        article.setMarkdownContent(dto.getMarkdownContent());
        article.setTypeId(dto.getTypeId());
        // 如果不存在，那么设置为默认封面
        if (StringUtils.isEmpty(dto.getCoverUrl())) {
            article.setCoverUrl(configService.getConfigByKey(ConfigKeyEnum.DEFAULT_AVATAR_URL.name()).getValue());
        }
        if (articleMapper.insert(article) <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 处理博客标签
        List<String> tagList = dto.getTagList();
        if (!CollectionUtils.isEmpty(tagList)) {
            tagList.forEach(tag -> {
                Tag t = tagService.getTagByName(tag);
                if (t != null) {
                    // 插入tag
                    TagAddDTO tagAddDTO = new TagAddDTO();
                    tagAddDTO.setName(tag);
                    t = tagService.addTag(tagAddDTO);
                }
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(t.getId());
                articleTagService.insertArticleTag(articleTag);
            });
        }
    }

    /**
     * 分页获取博客文章列表
     *
     * @param dto 分页获取博客文章列表DTO对象
     * @return 博客文章列表VO对象分页对象
     * @author Ling, Jiatong
     */
    public IPage<ArticleListVO> getList(ArticleListQueryDTO dto) {
        try {
            dto.generateSortCondition();
        } catch (IllegalArgumentException e) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        return articleMapper.getList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto);
    }

    /**
     * 更新博客信息
     *
     * @param dto 更新博客文章DTO对象
     * @author Ling, Jiatong
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleUpdateDTO dto) {
        // 校验博客文章是否存在
        Article article = checkExist(dto.getId());
        // 基础参数校验
        checkArticleAddAndUpdateDTO(dto);
        // 校验用户是否存在
        if (!UserConstant.ANONYMOUS_USER_ID.equals(dto.getUserId())) {
            userService.checkExist(dto.getUserId());
        }
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setModifyTime(new Date());
        article.setRecommend(dto.getRecommend());
        article.setUserId(dto.getUserId());
        article.setMarkdownContent(dto.getMarkdownContent());
        article.setTypeId(dto.getTypeId());
        // 如果不存在，那么设置为默认封面
        if (StringUtils.isEmpty(dto.getCoverUrl())) {
            article.setCoverUrl(configService.getConfigByKey(ConfigKeyEnum.DEFAULT_AVATAR_URL.name()).getValue());
        }
        if (articleMapper.insert(article) <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
        // 处理文章标签
        List<String> newTagNameList = dto.getTagList();
        // 先删除原集合，然后再插入新集合
        articleTagService.deleteArticleTagByArticleId(dto.getId());
        if (CollectionUtils.isEmpty(newTagNameList)) {
            return;
        }
        List<Integer> newTagIdList = tagService.getIdListByNameList(newTagNameList);
        newTagIdList.forEach(tagId -> {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagId(tagId);
            articleTag.setArticleId(dto.getId());
            articleTagService.insertArticleTag(articleTag);
        });
    }

    /**
     * 批量更新博客推荐状态
     *
     * @param dto 批量更新博客推荐状态DTO对象
     * @author Ling, Jiatong
     */
    public void updateRecommendBatch(ArticleUpdateRecommendBatchDTO dto) {
        List<Integer> idList = dto.getBatchKey();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        if (!List.of(BlogConstant.RECOMMEND, BlogConstant.NOT_RECOMMEND).contains(dto.getRecommend())) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                .set(Article::getRecommend, dto.getRecommend())
                .set(Article::getModifyTime, new Date())
                .in(Article::getId, idList));
    }

    /**
     * 获取博客详情
     *
     * @param dto 博客详情DTO对象
     * @return 博客详情VO对象
     * @author Ling, Jiatong
     */
    public ArticleDetailVO getDetail(ArticleDetailDTO dto) {
        // 校验是否存在
        checkExist(dto.getId());
        ArticleDetailVO vo = articleMapper.getDetail(dto);
        // 获取博客的标签列表
        List<Tag> tagList = tagService.getTagListByArticleId(vo.getId());
        if (CollectionUtils.isEmpty(tagList)) {
            tagList = List.of();
        }
        vo.setTagList(tagList);
        return vo;
    }

    //*********************************** 私有函数 ***********************************//

    /**
     * 新增|更新博客DTO参数校验
     *
     * @param dto 新增博客|更新博客DTO对象
     * @author Ling, Jiatong
     */
    private void checkArticleAddAndUpdateDTO(ArticleAddAndUpdateBaseDTO dto) {
        if (StringUtils.isEmpty(dto.getTitle())) {
            throw new ParamException(GlobalErrorEnum.BLOG_TITLE_EMPTY_ERROR);
        }
        if (dto.getTitle().length() < BlogConstant.BLOG_TITLE_MIN_LENGTH || dto.getTitle().length() > BlogConstant.BLOG_TITLE_MAX_LENGTH) {
            throw new ParamException(GlobalErrorEnum.BLOG_TITLE_LENGTH_ERROR);
        }
        if (StringUtils.isEmpty(dto.getMarkdownContent())) {
            throw new ParamException(GlobalErrorEnum.BLOG_MARKDOWN_CONTENT_EMPTY_ERROR);
        }
        // 如果发表用户为null，设置为匿名用户
        if (dto.getUserId() == null) {
            dto.setUserId(UserConstant.ANONYMOUS_USER_ID);
        }
        // 校验博客分类
        checkTypeId(dto.getTypeId());
    }

    //*********************************** 公用函数 ***********************************//

    /**
     * 校验博客类型是否存在
     *
     * @param typeId 博客类型id
     * @author Ling, Jiatong
     */
    public void checkTypeId(Integer typeId) {
        typeService.checkExist(typeId);
    }

    /**
     * 校验博客文章是否存在
     * 不存在抛出异常，存在返回该博客文章对象
     *
     * @param id 博客文章id
     * @author Ling, Jiatong
     */
    public Article checkExist(Integer id) {
        if (id == null) {
            throw new ResourceNotExistException(GlobalErrorEnum.BLOG_ARTICLE_NOT_EXIST_ERROR);
        }
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, id)
                .eq(Article::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        Optional.ofNullable(article)
                .orElseThrow(() -> new ResourceNotExistException(GlobalErrorEnum.BLOG_ARTICLE_NOT_EXIST_ERROR));
        return article;
    }


    //*********************************** 删除相关 ***********************************//

    /**
     * 批量逻辑删除博客文章
     *
     * @param dto 博客文章批量删除DTO对象
     * @author Ling, Jiatong
     */
    public void logicDeleteBatch(ArticleDeleteBatchDTO dto) {
        List<Integer> idList = dto.getBatchKey();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        // 逻辑删除博客文章
        articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                .set(Article::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_DELETED.getValue())
                .set(Article::getModifyTime, new Date())
                .in(Article::getId, idList));
    }

    /**
     * 批量物理删除博客文章
     *
     * @param dto 博客文章批量删除DTO对象
     * @author Ling, Jiatong
     */
    @Transactional(rollbackFor = Exception.class)
    public void physicDeleteBatch(ArticleDeleteBatchDTO dto) {
        // 物理删除博客文章
        List<Integer> idList = dto.getBatchKey();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        // 删除文章数据
        articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                .set(Article::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_DELETED.getValue())
                .set(Article::getModifyTime, new Date())
                .in(Article::getId, idList));
        // 删除博客文章和标签对应关系
        articleTagService.deleteArticleTagByArticleIdList(idList);
    }


    //*********************************** 其他函数 ***********************************//

}
