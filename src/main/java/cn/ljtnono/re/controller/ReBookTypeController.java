package cn.ljtnono.re.controller;

import cn.ljtnono.re.entity.ReBookType;
import cn.ljtnono.re.pojo.JsonResult;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReBookTypeController {


    public JsonResult listEntityAll() {
        return null;
    }


    public JsonResult saveEntity(ReBookType entity) {
        return null;
    }


    public JsonResult updateEntityById(Serializable id, ReBookType entity) {
        return null;
    }


    public JsonResult deleteEntityById(Serializable id) {
        return null;
    }


    public JsonResult getEntityById(Serializable id) {
        return null;
    }
}
