package cn.ljtnono.re.controller;


import cn.ljtnono.re.entity.ReBook;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.pojo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 书籍controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/book")
@Slf4j
public class ReBookController {

    public JsonResult listEntityAll() {
        return null;
    }


    public JsonResult saveEntity(ReBook entity) {
        return null;
    }


    public JsonResult updateEntityById(Serializable id, ReBook entity) {
        return null;
    }


    public JsonResult deleteEntityById(Serializable id) {
        return null;
    }


    public JsonResult getEntityById(Serializable id) {
        return null;
    }
}
