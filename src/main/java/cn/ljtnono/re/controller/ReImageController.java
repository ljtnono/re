package cn.ljtnono.re.controller;


import cn.ljtnono.re.entity.ReImage;
import cn.ljtnono.re.pojo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 图片controller
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@RestController
@RequestMapping("/image")
@Slf4j
public class ReImageController {


    /**
     * 普通的上传图片接口
     * @param multipartFile 封装的文件信息
     * @return {@link JsonResult}
     */
    @PostMapping("/upload")
    public JsonResult uploadImage(@RequestParam("file") MultipartFile multipartFile) {

        return null;
    }

    @PostMapping("/upload/md")
    public JsonResult uploadImageFromMarkdownEditor(@RequestParam("") MultipartFile multipartFile) {
        return null;
    }


    public JsonResult listEntityAll() {
        return null;
    }


    public JsonResult saveEntity(ReImage entity) {
        return null;
    }


    public JsonResult updateEntityById(Serializable id, ReImage entity) {
        return null;
    }


    public JsonResult deleteEntityById(Serializable id) {
        return null;
    }


    public JsonResult getEntityById(Serializable id) {
        return null;
    }
}
