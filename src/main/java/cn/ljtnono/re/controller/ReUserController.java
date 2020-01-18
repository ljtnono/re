package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.*;
import cn.ljtnono.re.entity.ReUser;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReUserService;
import cn.ljtnono.re.util.EncryptUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReUserController {

    private IReUserService iReUserService;

    @Autowired
    public ReUserController(IReUserService iReUserService) {
        this.iReUserService = iReUserService;
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取用户对象", httpMethod = "GET")
    public JsonResult getEntityById(@PathVariable Serializable id) {
        return iReUserService.getEntityById(id);
    }

    @GetMapping
    @ApiOperation(value = "查询所有用户列表", httpMethod = "GET")
    public JsonResult listEntityAll() {
        return iReUserService.listEntityAll();
    }

    @PostMapping
    @ApiOperation(value = "新增一个用户", httpMethod = "POST")
    public JsonResult saveEntity(ReUserSaveDTO reUserSaveDTO) {
        ReUser entity = new ReUser();
        entity.setEmail(reUserSaveDTO.getEmail());
        entity.setPassword(EncryptUtil.getInstance().getMd5LowerCase(reUserSaveDTO.getPassword()));
        entity.setQq(reUserSaveDTO.getQq());
        entity.setTel(reUserSaveDTO.getTel());
        entity.setStatus((byte) 1);
        entity.setCreateTime(new Date());
        entity.setModifyTime(new Date());
        return iReUserService.saveEntity(entity);
    }

    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id更新用户", httpMethod = "PUT")
    public JsonResult updateEntityById(@PathVariable(value = "id") Serializable id, ReUserUpdateDTO reUserUpdateDTO) {
        ReUser entity = new ReUser();
        entity.setEmail(reUserUpdateDTO.getEmail());
        entity.setPassword(EncryptUtil.getInstance().getMd5LowerCase(reUserUpdateDTO.getPassword()));
        entity.setQq(reUserUpdateDTO.getQq());
        entity.setTel(reUserUpdateDTO.getTel());
        entity.setStatus((byte) 1);
        return iReUserService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    public JsonResult deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReUserService.deleteEntityById(id);
    }

    @PutMapping("/restore/{id:\\d+}")
    @ApiOperation(value = "恢复删除的用户", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResult restore(@PathVariable(value = "id") Serializable id) {
        return iReUserService.restore(id);
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据username、tel和email模糊查询", notes = "根据username、tel和email模糊查询", httpMethod = "POST")
    public JsonResult search(ReUserSearchDTO reUserSearchDTO, @Validated PageDTO pageDTO) {
        return iReUserService.search(reUserSearchDTO, pageDTO);
    }

    @GetMapping("/listUserPage")
    @ApiOperation(value = "分页查询用户列表", httpMethod = "GET")
    public JsonResult listUserPage(@Validated PageDTO pageDTO) {
        return iReUserService.listUserPage(pageDTO.getPage(), pageDTO.getCount());
    }
}
