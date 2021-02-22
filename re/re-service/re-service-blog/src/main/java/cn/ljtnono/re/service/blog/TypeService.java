package cn.ljtnono.re.service.blog;

import cn.ljtnono.re.common.constant.blog.BlogConstant;
import cn.ljtnono.re.common.enumeration.EntityConstantEnum;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.GlobalException;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.dto.blog.type.TypeAddDTO;
import cn.ljtnono.re.entity.blog.Type;
import cn.ljtnono.re.mapper.blog.TypeMapper;
import cn.ljtnono.re.vo.blog.type.TypeDetailVO;
import cn.ljtnono.re.vo.blog.type.TypeSelectVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 博客类型模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/2/22 12:59 上午
 */
@Service
public class TypeService {

    @Resource
    private TypeMapper typeMapper;

    //*********************************** 接口调用 ***********************************//

    /**
     * 新增博客类型
     *
     * @param dto 新增博客类型DTO对象
     */
    public void addType(TypeAddDTO dto) {
        // 校验是否重复
        checkNameDuplicate(dto.getName());
        // 校验是否推荐参数
        if (!Arrays.asList(BlogConstant.NOT_RECOMMEND, BlogConstant.RECOMMEND).contains(dto.getRecommend())) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        Type type = new Type();
        type.setDeleted(EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        type.setFavorite(0);
        type.setView(0);
        type.setCreateTime(new Date());
        type.setModifyTime(new Date());
        type.setName(dto.getName());
        type.setRecommend(dto.getRecommend());
        if (typeMapper.insert(type) <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    /**
     * 获取博客类型下拉列表
     *
     * @return 博客类型下拉列表VO对象
     * @author Ling, Jiatong
     */
    public List<TypeSelectVO> select() {
        List<Type> typeList = typeMapper.selectList(new LambdaQueryWrapper<Type>()
                .select(Type::getId, Type::getName)
                .eq(Type::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        return typeList
                .stream()
                .map(type -> {
                    TypeSelectVO vo = new TypeSelectVO();
                    BeanUtils.copyProperties(type, vo);
                    return vo;
                }).collect(Collectors.toList());
    }

    /**
     * 根据id获取博客类型详情
     *
     * @param id 博客类型id
     * @return 博客类型详情VO对象
     * @author Ling, Jiatong
     */
    public TypeDetailVO getDetailById(Integer id) {
        Type type = checkExist(id);
        TypeDetailVO vo = new TypeDetailVO();
        BeanUtils.copyProperties(type, vo);
        return vo;
    }



    //*********************************** 私有方法 ***********************************//


    //*********************************** 公共方法 ***********************************//

    /**
     * 根据类型名校验类型是否重复
     * 只对比没有删除的
     *
     * @param name 博客类型名
     */
    public void checkNameDuplicate(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new GlobalException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        Type type = typeMapper.selectOne(new LambdaQueryWrapper<Type>()
                .eq(Type::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue())
                .eq(Type::getName, name));
        if (type != null) {
            throw new ParamException(GlobalErrorEnum.BLOG_TYPE_ALREADY_EXIST_ERROR);
        }
    }

    /**
     * 校验是否存在
     * 不存在抛出异常，存在返回该对象
     *
     * @param id 博客类型id
     * @return 存在则返回该博客类型实体对象
     * @author Ling, Jiatong
     */
    public Type checkExist(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        Type type = typeMapper.selectOne(new LambdaQueryWrapper<Type>()
                .eq(Type::getId, id)
                .eq(Type::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()));
        Optional.ofNullable(type)
                .orElseThrow(() -> new ResourceNotExistException(GlobalErrorEnum.BLOG_TYPE_NOT_EXIST_ERROR));
        return type;
    }

    //*********************************** 其他方法 ***********************************//


}
