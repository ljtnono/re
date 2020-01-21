package cn.ljtnono.re.controller;

import cn.ljtnono.re.entity.ReImage;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReImageService;
import cn.ljtnono.re.util.FtpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 图片controller
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.2
 */
@RestController
@RequestMapping("/image")
@Slf4j
public class ReImageController {


    private IReImageService iReImageService;

    private final FtpClientUtil ftpClientUtil;

    @Autowired
    public ReImageController(IReImageService iReImageService, FtpClientUtil ftpClientUtil) {
        this.iReImageService = iReImageService;
        this.ftpClientUtil = ftpClientUtil;
    }

    @PostMapping("/upload")
    public JsonResult uploadImage(MultipartFile multipartFile)  {
        multipartFile.getContentType();


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
