package cn.ljtnono.re.controller;

import cn.ljtnono.re.entity.ReBookType;
import cn.ljtnono.re.vo.JsonResultVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 书籍类型controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/book_type")
@Api(value = "ReBookTypeController", tags = {"书籍类型接口"})
public class ReBookTypeController {

    public JsonResultVO listEntityAll() {
        return null;
    }


    public JsonResultVO saveEntity(ReBookType entity) {
        return null;
    }


    public JsonResultVO updateEntityById(Serializable id, ReBookType entity) {
        return null;
    }


    public JsonResultVO deleteEntityById(Serializable id) {
        return null;
    }


    public JsonResultVO getEntityById(Serializable id) {
        return null;
    }
}
