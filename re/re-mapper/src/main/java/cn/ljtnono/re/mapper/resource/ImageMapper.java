package cn.ljtnono.re.mapper.resource;

import cn.ljtnono.re.dto.resource.image.ImageListQueryDTO;
import cn.ljtnono.re.entity.resource.Image;
import cn.ljtnono.re.vo.resource.image.ImageListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 图片模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2021/1/1 23:54
 */
public interface ImageMapper extends BaseMapper<Image> {

    /**
     * 获取图片列表
     *
     * @param page 分页对象
     * @param dto 图片列表查询DTO对象
     * @return 图片列表VO对象分页对象
     * @author Ling, Jiatong
     */
    IPage<ImageListVO> getList(Page<?> page, @Param("dto") ImageListQueryDTO dto);
}
