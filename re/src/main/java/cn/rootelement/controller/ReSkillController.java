package cn.rootelement.controller;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReSkillSaveDTO;
import cn.rootelement.dto.ReSkillSearchDTO;
import cn.rootelement.dto.ReSkillUpdateDTO;
import cn.rootelement.entity.ReSkill;
import cn.rootelement.service.IReSkillService;
import cn.rootelement.vo.JsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 技能controller
 * @author ljt
 * @date 2020/1/14
 * @version 1.0.2
 */
@RestController
@RequestMapping("/skill")
@Api(value = "ReSkillController", tags = {"技能接口"})
public class ReSkillController {

    private IReSkillService iReSkillService;

    @Autowired
    public ReSkillController(IReSkillService iReSkillService) {
        this.iReSkillService = iReSkillService;
    }

    @GetMapping
    @ApiOperation(value = "获取所有的技能列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReSkillService.listEntityAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "新增技能", httpMethod = "POST")
    public JsonResultVO saveEntity(@Validated ReSkillSaveDTO reSkillSaveDTO) {
        ReSkill reSkill = new ReSkill();
        BeanUtils.copyProperties(reSkillSaveDTO, reSkill);
        reSkill.setStatus((byte) 1);
        reSkill.setCreateTime(new Date());
        reSkill.setModifyTime(new Date());
        return iReSkillService.saveEntity(reSkill);
    }

    @PutMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "根据id更新一个技能实体", notes = "id只能是数字类型", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReSkillUpdateDTO reSkillUpdateDTO) {
        ReSkill reSkill = new ReSkill();
        BeanUtils.copyProperties(reSkillUpdateDTO, reSkill);
        reSkill.setStatus((byte) 1);
        return iReSkillService.updateEntityById(id, reSkill);
    }

    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "根据id删除一个skill记录", notes = "id只能为数字类型", httpMethod = "DELETE")
    public JsonResultVO deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReSkillService.deleteEntityById(id);
    }

    @PutMapping("/restore/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "恢复删除的技能", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReSkillService.restore(id);
    }


    @GetMapping("/{id:\\d+}")
    public JsonResultVO getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReSkillService.getEntityById(id);
    }

    @GetMapping("/listSkillPage")
    @ApiOperation(value = "分页查询技能列表", httpMethod = "GET")
    public JsonResultVO listSkillPage(@Validated PageDTO pageDTO) {
        return iReSkillService.listSkillPage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接name和owner模糊查询", notes = "根据链接name和owner模糊查询", httpMethod = "POST")
    public JsonResultVO search(ReSkillSearchDTO reSkillSearchDTO, @Validated PageDTO pageDTO) {
        return iReSkillService.search(reSkillSearchDTO, pageDTO);
    }
}
