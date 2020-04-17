package cn.rootelement.controller;

import cn.rootelement.entity.ReBook;
import cn.rootelement.vo.JsonResultVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 书籍controller
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.1
 */
@RestController
@RequestMapping("/book")
@Api(value = "ReBookController", tags = {"书籍接口"})
public class ReBookController {

    public JsonResultVO listEntityAll() {
        return null;
    }


    public JsonResultVO saveEntity(ReBook entity) {
        return null;
    }


    public JsonResultVO updateEntityById(Serializable id, ReBook entity) {
        return null;
    }


    public JsonResultVO deleteEntityById(Serializable id) {
        return null;
    }


    public JsonResultVO getEntityById(Serializable id) {
        return null;
    }
}
