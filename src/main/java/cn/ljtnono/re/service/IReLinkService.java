package cn.ljtnono.re.service;

import cn.ljtnono.re.entity.ReLink;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.common.IReEntityService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 链接服务接口
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
public interface IReLinkService extends IService<ReLink>, IReEntityService<ReLink> {

    /**
     * 获取所有外部链接数据
     * @return 所有外部链接数据
     */
    List<ReLink> listOutLinkAll();

    /**
     * 分页获取链接列表
     * @param page 页数
     * @param count 每页获取的条数
     * @return JsonResult对象
     */
    JsonResult listLinkPage(Integer page, Integer count);
}
