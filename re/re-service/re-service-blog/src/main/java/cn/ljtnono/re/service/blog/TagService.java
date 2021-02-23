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
import org.springframework.stereotype.Service;
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


    //*********************************** 接口调用 ***********************************//

    /**
     * 新增博客标签
     *
     * @param dto 新增博客标签DTO对象
     * @author Ling, Jiatong
     */
    public void addTag(TagAddDTO dto) {
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
        }
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



    //*********************************** 其他函数 ***********************************//



}
