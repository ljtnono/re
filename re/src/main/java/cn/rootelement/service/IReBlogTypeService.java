package cn.rootelement.service;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.entity.ReBlogType;
import cn.rootelement.service.common.IReEntityService;
import cn.rootelement.vo.JsonResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * 博客类型服务接口
 * @author ljt
 * @date 2020/1/6
 * @version 1.0.3
 */
public interface IReBlogTypeService extends IService<ReBlogType>, IReEntityService<ReBlogType> {


    /**
     * 分页获取博客类型
     * @param page 页码
     * @param count 每页显示的条数
     * @return JsonResult 对象
     */
    JsonResultVO listBlogTypePage(Integer page, Integer count);

    /**
     * 恢复删除的博客类型
     * @param id 需要恢复的博客类型id
     * @return JsonResult 对象
     */
    JsonResultVO restore(Serializable id);

    /**
     * 博客类型名称模糊查询
     * @param name 博客类型名称
     * @param pageDTO 页码对象
     * @return JsonResult 对象
     */
    JsonResultVO search(final String name, PageDTO pageDTO);
}
