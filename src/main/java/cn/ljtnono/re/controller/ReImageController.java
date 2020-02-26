package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReImageSaveDTO;
import cn.ljtnono.re.dto.ReImageSearchDTO;
import cn.ljtnono.re.dto.ReImageUpdateDTO;
import cn.ljtnono.re.entity.ReImage;
import cn.ljtnono.re.enumeration.HttpStatusEnum;
import cn.ljtnono.re.enumeration.GlobalVariableEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.service.IReImageService;
import cn.ljtnono.re.util.FtpClientUtil;
import cn.ljtnono.re.util.StringUtil;
import cn.ljtnono.re.vo.JsonResultVO;
import cn.ljtnono.re.vo.MarkdownEditorUploadImageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 图片controller
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.2
 */
@Slf4j
@RestController
@RequestMapping("/image")
@Api(value = "ReImageController", tags = {"图片接口"})
public class ReImageController {

    private IReImageService iReImageService;

    private FtpClientUtil ftpClientUtil;

    @Autowired
    public ReImageController(IReImageService iReImageService, FtpClientUtil ftpClientUtil) {
        this.iReImageService = iReImageService;
        this.ftpClientUtil = ftpClientUtil;
    }

    /**
     *
     * 根据MultipartFile 生成一个ReImage对象
     * @param multipartFile MultipartFile对象
     * @throws NullPointerException 当multiPartFile为null时抛出
     * @return ReImage对象
     */
    private ReImage getReImageFromMultipartFile(MultipartFile multipartFile) {
        Objects.requireNonNull(multipartFile, "multipartFile不能为null");
        String originalFilename = multipartFile.getOriginalFilename();
        Objects.requireNonNull(originalFilename);
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        ReImage reImage = new ReImage();
        reImage.setId(StringUtil.getUUIDWithoutJoiner());
        reImage.setCreateTime(new Date());
        reImage.setModifyTime(new Date());
        reImage.setStatus((byte) 1);
        reImage.setSize(multipartFile.getSize());
        reImage.setOriginName(originalFilename.substring(0, originalFilename.lastIndexOf(".")));
        reImage.setType(extName);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String name = authentication.getName();
            reImage.setOwner(name);
        } else {
            reImage.setOwner("ljtnono");
        }
        reImage.setUrl(GlobalVariableEnum.RE_FTP_SAVE_PREFIX.getValue() + "/images/" + originalFilename);
        return reImage;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "上传图片接口", httpMethod = "POST")
    public JsonResultVO uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        JsonResultVO jsonResultVO;
        Optional.ofNullable(multipartFile).orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        try {
            boolean b = ftpClientUtil.uploadFile(multipartFile);
            if (b) {
                // 创建一个image对象
                ReImage reImage = getReImageFromMultipartFile(multipartFile);
                jsonResultVO = iReImageService.saveEntity(reImage);
                // 如果存储失败，那么删除
                if (!jsonResultVO.getStatus().equals(HttpStatusEnum.OK.getCode())) {
                    ftpClientUtil.deleteFile("images", multipartFile.getOriginalFilename());
                } else {
                    jsonResultVO.setData(Collections.singleton(reImage));
                }
            } else {
                jsonResultVO = JsonResultVO.fail(HttpStatusEnum.INTERNAL_SERVER_ERROR.getCode());
            }
        } catch (Exception e) {
            log.error("图片 {} 上传失败", multipartFile.getOriginalFilename());
            jsonResultVO = JsonResultVO.fail(HttpStatusEnum.INTERNAL_SERVER_ERROR.getCode());
        }
        return jsonResultVO;
    }

    @PostMapping("/upload/md")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "editor上传图片接口", notes = "这里参数只能为editormd-image-file", httpMethod = "POST")
    public MarkdownEditorUploadImageVO uploadImageFromMarkdownEditor(@RequestParam("editormd-image-file") MultipartFile multipartFile) {
        Optional.ofNullable(multipartFile).orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        MarkdownEditorUploadImageVO result = new MarkdownEditorUploadImageVO();
        try {
            boolean b = ftpClientUtil.uploadFile(multipartFile);
            if (b) {
                // 创建一个image对象
                ReImage reImage = getReImageFromMultipartFile(multipartFile);
                JsonResultVO jsonResultVO = iReImageService.saveEntity(reImage);
                if (jsonResultVO.getStatus().equals(HttpStatusEnum.OK.getCode())) {
                    result.setUrl(reImage.getUrl());
                    result.setSuccess(1);
                    result.setMessage("上传成功");
                } else {
                    ftpClientUtil.deleteFile("images", multipartFile.getOriginalFilename());
                    result.setSuccess(0);
                    result.setMessage("上传失败");
                }
            } else {
                log.error("图片 {} 上传失败", multipartFile.getOriginalFilename());
                result.setSuccess(0);
                result.setMessage("上传失败");
            }
        } catch (Exception e) {
            log.error("图片 {} 上传失败", multipartFile.getOriginalFilename());
            result.setSuccess(0);
            result.setMessage("上传失败");
        }
        return result;
    }


    @GetMapping
    @ApiOperation(value = "获取所有图片列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReImageService.listEntityAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('root')")
    public JsonResultVO saveEntity(@Validated ReImageSaveDTO reImageSaveDTO) {
        return null;
    }

    @PutMapping("/{id:\\d+}")
    public JsonResultVO updateEntityById(@PathVariable("id") Serializable id, ReImageUpdateDTO reImageUpdateDTO) {
        return null;
    }

    @DeleteMapping("/{id:\\d+}")
    public JsonResultVO deleteEntityById(@PathVariable("id") Serializable id) {
        return null;
    }

    @GetMapping("/{id:\\w+}")
    @ApiOperation(value = "根据id获取一个图片", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable("id") Serializable id) {
        return iReImageService.getEntityById(id);
    }

    @GetMapping("/listImagePage")
    @ApiOperation(value = "分页获取图片列表", httpMethod = "GET")
    public JsonResultVO listImagePage(@Validated PageDTO pageDTO) {
        return iReImageService.listImagePage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接originName和url还有type模糊查询", notes = "根据链接originName和url还有type模糊查询", httpMethod = "POST")
    public JsonResultVO search(ReImageSearchDTO reImageSearchDTO, @Validated PageDTO pageDTO) {
        return iReImageService.search(reImageSearchDTO, pageDTO);
    }
}