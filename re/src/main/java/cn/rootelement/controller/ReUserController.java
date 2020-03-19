package cn.rootelement.controller;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReUserSaveDTO;
import cn.rootelement.dto.ReUserSearchDTO;
import cn.rootelement.dto.ReUserUpdateDTO;
import cn.rootelement.entity.ReUser;
import cn.rootelement.service.IReUserService;
import cn.rootelement.util.Md5Util;
import cn.rootelement.vo.JsonResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户controller
 * @author ljt
 * @date 2020/1/18
 * @version 1.0.2
 */
@RestController
@RequestMapping("/user")
@Api(value = "ReUserController", tags = {"用户接口"})
public class ReUserController {

    private IReUserService iReUserService;

    @Autowired
    public ReUserController(IReUserService iReUserService) {
        this.iReUserService = iReUserService;
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取用户对象", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable Serializable id) {
        return iReUserService.getEntityById(id);
    }

    @GetMapping
    @ApiOperation(value = "查询所有用户列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReUserService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个用户", httpMethod = "POST")
    public JsonResultVO saveEntity(@Validated ReUserSaveDTO reUserSaveDTO) {
        ReUser entity = new ReUser();
        BeanUtils.copyProperties(reUserSaveDTO, entity);
        entity.setPassword(Md5Util.getInstance().getMd5LowerCase(reUserSaveDTO.getPassword()));
        entity.setStatus((byte) 1);
        entity.setCreateTime(new Date());
        entity.setModifyTime(new Date());
        return iReUserService.saveEntity(entity);
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新用户", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReUserUpdateDTO reUserUpdateDTO) {
        ReUser entity = new ReUser();
        BeanUtils.copyProperties(reUserUpdateDTO, entity);
        entity.setPassword(Md5Util.getInstance().getMd5LowerCase(reUserUpdateDTO.getPassword()));
        entity.setStatus((byte) 1);
        return iReUserService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    public JsonResultVO deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReUserService.deleteEntityById(id);
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的用户", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReUserService.restore(id);
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据username、tel和email模糊查询", notes = "根据username、tel和email模糊查询", httpMethod = "POST")
    public JsonResultVO search(ReUserSearchDTO reUserSearchDTO, @Validated PageDTO pageDTO) {
        return iReUserService.search(reUserSearchDTO, pageDTO);
    }

    @GetMapping("/listUserPage")
    @ApiOperation(value = "分页查询用户列表", httpMethod = "GET")
    public JsonResultVO listUserPage(@Validated PageDTO pageDTO) {
        return iReUserService.listUserPage(pageDTO.getPage(), pageDTO.getCount());
    }
}
