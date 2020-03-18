package cn.ljtnono.re.service;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReSkillSearchDTO;
import cn.ljtnono.re.entity.ReSkill;
import cn.ljtnono.re.service.common.IReEntityService;
import cn.ljtnono.re.vo.JsonResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * 技能类服务接口
 * @author ljt
 * @date 2020/1/14
 * @version 1.0.1
 */
public interface IReSkillService extends IService<ReSkill>, IReEntityService<ReSkill> {

    /**
     * 无条件分页查询skill列表
     * @param page 页数
     * @param count 每页条数
     * @return JsonResult 对象
     */
    JsonResultVO listSkillPage(Integer page, Integer count);

    /**
     * 恢复删除的技能
     * @param id 需要恢复的链接id
     * @return JsonResult 对象
     */
    JsonResultVO restore(Serializable id);

    /**
     * 技能分页条件查询
     * @param reSkillSearchDTO 条件查询条件DTO
     * @param pageDTO 分页对象
     * @return JsonResult 对象
     */
    JsonResultVO search(ReSkillSearchDTO reSkillSearchDTO, PageDTO pageDTO);
}
