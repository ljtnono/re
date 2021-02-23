package cn.ljtnono.re.service.blog;

import cn.ljtnono.re.common.constant.blog.BlogConstant;
import cn.ljtnono.re.common.enumeration.EntityConstantEnum;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.ResourceNotExistException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.dto.blog.type.*;
import cn.ljtnono.re.entity.blog.Type;
import cn.ljtnono.re.mapper.blog.TypeMapper;
import cn.ljtnono.re.vo.blog.type.TypeDetailVO;
import cn.ljtnono.re.vo.blog.type.TypeListQueryVO;
import cn.ljtnono.re.vo.blog.type.TypeSelectVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
        // 校验类型名长度
        checkNameLength(dto.getName());
        // 校验是否推荐参数
        checkRecommendValue(dto.getRecommend());
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

    /**
     * 分页获取博客类型列表
     *
     * @param dto 分页获取博客类型列表DTO对象
     * @return 博客类型列表VO对象分页对象
     * @author Ling, Jiatong
     */
    public IPage<TypeListQueryVO> getList(TypeListQueryDTO dto) {
        try {
            dto.generateSortCondition();
        } catch (IllegalArgumentException e) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        return typeMapper.getList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto);
    }

    /**
     * 批量更新博客类型推荐状态
     *
     * @param dto 批量更新博客类型推荐状态DTO对象
     * @author Ling, Jiatong
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRecommendBatch(TypeUpdateRecommendBatchDTO dto) {
        List<Integer> idList = dto.getBatchKey();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        checkRecommendValue(dto.getRecommend());
        // 校验是否都存在
        checkExist(idList);
        // 更新状态
        typeMapper.update(null, new LambdaUpdateWrapper<Type>()
                .set(Type::getRecommend, dto.getRecommend())
                .set(Type::getModifyTime, new Date())
                .in(Type::getId, idList));
        // TODO 更新该类型下所有的博客的推荐状态
    }


    /**
     * 更新博客类型
     *
     * @param dto 更新博客类型DTO对象
     * @author Ling, Jiatong
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateType(TypeUpdateDTO dto) {
        // 校验是否存在
        Type type = checkExist(dto.getId());
        if (!StringUtils.isEmpty(dto.getName())) {
            checkNameLength(dto.getName());
            // 如果是更改标签名，那么需要校验是否重名
            checkNameDuplicate(dto.getName());
            type.setName(dto.getName());
            type.setModifyTime(new Date());
        }
        if (dto.getRecommend() != null) {
            checkRecommendValue(dto.getRecommend());
            type.setRecommend(dto.getRecommend());
            type.setModifyTime(new Date());
            // TODO 更新博客类型下面所有博客的推荐状态
        }
        typeMapper.updateById(type);
    }

    //*********************************** 私有方法 ***********************************//

    /**
     * 校验推荐值是否合法
     *
     * @param recommend 推荐值
     * @author Ling, Jiatong
     */
    private void checkRecommendValue(Integer recommend) {
        // 校验推荐状态值是否合法
        if (List.of(BlogConstant.RECOMMEND, BlogConstant.NOT_RECOMMEND).contains(recommend)) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 校验博客类型名长度
     * 超过长度抛出异常
     *
     * @param name 博客类型名
     * @author Ling, Jiatong
     */
    private void checkNameLength(String name) {
        if (!StringUtils.isEmpty(name) && name.length() > BlogConstant.BLOG_TYPE_NAME_MAX_LENGTH) {
            throw new ParamException(GlobalErrorEnum.BLOG_TYPE_NAME_LENGTH_ERROR);
        }
    }

    //*********************************** 公共方法 ***********************************//

    /**
     * 根据类型名校验类型是否重复
     * 只对比没有删除的
     *
     * @param name 博客类型名
     */
    public void checkNameDuplicate(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
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

    /**
     * 校验是否存在
     * 有一个不存在则抛出异常，都存在返回该博客类型列表
     *
     * @param idList 博客类型id列表
     * @return 博客类型列表
     * @author Ling, Jiatong
     */
    public List<Type> checkExist(List<Integer> idList) {
        List<Type> typeList = typeMapper.selectList(new LambdaQueryWrapper<Type>()
                .eq(Type::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue())
                .in(Type::getId, idList));
        if (typeList.size() != idList.size()) {
            throw new ResourceNotExistException(GlobalErrorEnum.BLOG_TYPE_NOT_EXIST_ERROR);
        }
        return typeList;
    }

    //*********************************** 删除相关 ***********************************//

    /**
     * 批量逻辑删除博客类型
     *
     * @param dto 博客类型批量删除DTO对象
     * @author Ling, Jiatong
     */
    @Transactional(rollbackFor = Exception.class)
    public void logicDeleteBatch(TypeDeleteBatchDTO dto) {
        List<Integer> idList = dto.getBatchKey();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        // TODO 逻辑删除相关博客
        // 逻辑删除博客类型
        typeMapper.update(null, new LambdaUpdateWrapper<Type>()
                .in(Type::getId, idList));
    }

    /**
     * 批量物理删除博客类型
     *
     * @param dto 博客类型批量删除DTO对象
     * @author Ling, Jiatong
     */
    @Transactional(rollbackFor = Exception.class)
    public void physicDeleteBatch(TypeDeleteBatchDTO dto) {
        List<Integer> idList = dto.getBatchKey();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        // TODO 物理删除相关博客
        // 物理删除博客类型
        typeMapper.delete(new LambdaQueryWrapper<Type>()
                .in(Type::getId, idList));
    }




    //*********************************** 其他方法 ***********************************//


}
