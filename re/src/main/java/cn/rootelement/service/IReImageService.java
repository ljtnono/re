package cn.rootelement.service;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReImageSearchDTO;
import cn.rootelement.entity.ReImage;
import cn.rootelement.service.common.IReEntityService;
import cn.rootelement.vo.JsonResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 图像类接口
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
public interface IReImageService extends IService<ReImage>, IReEntityService<ReImage> {

    /**
     * 分页查询图片列表
     * @param page 分页页码
     * @param count 每页显示条数
     * @return JsonResult 对象
     */
    JsonResultVO listImagePage(Integer page, Integer count);

    /**
     * 条件分页查询
     * @param reImageSearchDTO 条件对象
     * @param pageDTO 分页对象
     * @return JsonResult 对象
     */
    JsonResultVO search(ReImageSearchDTO reImageSearchDTO, PageDTO pageDTO);
}
