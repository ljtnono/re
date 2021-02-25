package cn.ljtnono.re.service.blog;

import cn.ljtnono.re.common.constant.blog.BlogConstant;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.dto.blog.tag.TagAddDTO;
import cn.ljtnono.re.entity.blog.Tag;
import cn.ljtnono.re.mapper.blog.TagMapper;
import cn.ljtnono.re.vo.blog.tag.TagSelectVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 博客标签模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/2/24 12:58 上午
 */
@Service
public class TagService {

    @Resource
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagService articleTagService;

    //*********************************** 接口调用 ***********************************//

    /**
     * 新增博客标签
     * 如果标签不存在，则新增后返回
     * 如果标签存在，则返回
     *
     * @param dto 新增博客标签DTO对象
     * @author Ling, Jiatong
     */
    public Tag addTag(TagAddDTO dto) {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new ParamException(GlobalErrorEnum.BLOG_TAG_NAME_EMPTY_ERROR);
        }
        if (dto.getName().length() > BlogConstant.BLOG_TAG_NAME_MAX_LENGTH) {
            throw new ParamException(GlobalErrorEnum.BLOG_TAG_NAME_LENGTH_ERROR);
        }
        Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, dto.getName()));
        if (tag == null) {
            Tag t = new Tag();
            t.setName(dto.getName());
            if (tagMapper.insert(t) <= 0) {
                throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
            }
            return t;
        }
        return tag;
    }

    /**
     * 获取博客标签下拉列表
     *
     * @return 博客标签下拉列表VO对象列表
     * @author Ling, Jiatong
     */
    public List<TagSelectVO> select() {
        return tagMapper.selectList(new LambdaQueryWrapper<Tag>())
                .stream()
                .map(tag -> {
                    TagSelectVO vo = new TagSelectVO();
                    BeanUtils.copyProperties(tag, vo);
                    return vo;
                }).collect(Collectors.toList());
    }



    //*********************************** 私有函数 ***********************************//


    //*********************************** 公用函数 ***********************************//

    /**
     * 根据标签名获取标签实体
     *
     * @param name 标签名
     * @author Ling, Jiatong
     */
    public Tag getTagByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getName, name));
    }

    /**
     * 获取博客文章下所有的标签列表
     * 不存在则返回空列表
     *
     * @param articleId 博客文章id
     * @return 标签列表
     * @author Ling, Jiatong
     */
    public List<Tag> getTagListByArticleId(Integer articleId) {
        List<Integer> tagIdList = articleTagService.getTagIdListByArticleId(articleId);
        if (CollectionUtils.isEmpty(tagIdList)) {
            return List.of();
        } else {
            List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getId, tagIdList));
            if (CollectionUtils.isEmpty(tagList)) {
                return List.of();
            }
            return tagList;
        }
    }

    /**
     * 根据标签名列表获取标签id列表
     * 不存在则返回空列表
     *
     * @param nameList 标签名列表
     * @return 标签id列表
     * @author Ling, Jiatong
     */
    public List<Integer> getIdListByNameList(List<String> nameList) {
        if (CollectionUtils.isEmpty(nameList)) {
            return List.of();
        }
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .in(Tag::getName, nameList));
        if (CollectionUtils.isEmpty(tagList)) {
            return List.of();
        }
        return tagList
                .stream()
                .map(Tag::getId)
                .distinct()
                .collect(Collectors.toList());
    }

    //*********************************** 其他函数 ***********************************//



}
