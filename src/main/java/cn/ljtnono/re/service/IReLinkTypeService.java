package cn.ljtnono.re.service;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.entity.ReLinkType;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.common.IReEntityService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * 链接类型服务接口
 * @author ljt
 * @date 2020/1/13
 * @version 1.0.2
 */
public interface IReLinkTypeService extends IService<ReLinkType>, IReEntityService<ReLinkType> {

    /**
     * 分页查询链接类型
     * @param page 分页查询页数
     * @param count 每页查询的条数
     * @return JsonResult 对象
     */
    JsonResult listLinkTypePage(Integer page, Integer count);

    /**
     * 恢复删除的链接类型
     * @param id 需要恢复的链接类型id
     * @return JsonResult 对象
     */
    JsonResult restore(Serializable id);

    /**
     * 链接类型名称模糊查询
     * @param name 链接类型名称
     * @param pageDTO 页码对象
     * @return JsonResult 对象
     */
    JsonResult search(final String name, PageDTO pageDTO);
}
