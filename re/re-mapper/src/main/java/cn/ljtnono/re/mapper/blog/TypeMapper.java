package cn.ljtnono.re.mapper.blog;

import cn.ljtnono.re.dto.blog.type.TypeListQueryDTO;
import cn.ljtnono.re.entity.blog.Type;
import cn.ljtnono.re.vo.blog.type.TypeListQueryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 博客类型模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2021/2/22 1:00 上午
 */
public interface TypeMapper extends BaseMapper<Type> {

    /**
     * 分页获取博客类型列表
     *
     * @param page 分页对象
     * @param dto 分页获取博客类型列表DTO对象
     * @return 分页获取博客类型列表VO对象分页对象
     * @author Ling, Jiatong
     */
    IPage<TypeListQueryVO> getList(Page<?> page, @Param("dto") TypeListQueryDTO dto);
}
