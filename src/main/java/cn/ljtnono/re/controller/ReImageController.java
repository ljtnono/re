package cn.ljtnono.re.controller;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReImageSearchDTO;
import cn.ljtnono.re.entity.ReImage;
import cn.ljtnono.re.enumeration.GlobalVariableEnum;
import cn.ljtnono.re.service.IReImageService;
import cn.ljtnono.re.util.FtpClientUtil;
import cn.ljtnono.re.util.StringUtil;
import cn.ljtnono.re.vo.JsonResultVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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
        String name = authentication.getName();
        reImage.setOwner(name);
        reImage.setUrl(GlobalVariableEnum.RE_FTP_SAVE_PREFIX.getValue() + "/images/" + originalFilename);
        return reImage;
    }

    @PostMapping("/upload")
    public JsonResultVO uploadImage(MultipartFile multipartFile)  {
        ReImage reImage = getReImageFromMultipartFile(multipartFile);

        return null;
    }

    @PostMapping("/upload/md")
    @ApiOperation(value = "editor上传图片接口", notes = "这里参数只能为editormd-image-file", httpMethod = "POST")
    public JSONObject uploadImageFromMarkdownEditor(@RequestParam(value = "editormd-image-file") MultipartFile multipartFile) {
        JSONObject result = new JSONObject();
        try {
            boolean b = ftpClientUtil.uploadFile(multipartFile);
            if (b) {
                // 创建一个image对象
                ReImage reImage = getReImageFromMultipartFile(multipartFile);
                JsonResultVO jsonResultVO = iReImageService.saveEntity(reImage);
                if (jsonResultVO.getStatus() == HttpStatus.OK.value()) {
                    result.put("url", reImage.getUrl());
                    result.put("success", 1);
                    result.put("message", "上传成功");
                } else {
                    result.put("success", 0);
                    result.put("message", "上传失败");
                }
            } else {
                log.error("图片 {} 上传失败", multipartFile.getOriginalFilename());
                result.put("success", 0);
                result.put("message", "上传失败");
            }
        } catch (Exception e) {
            log.error("图片 {} 上传失败", multipartFile.getOriginalFilename());
            result.put("success", 0);
            result.put("message", "上传失败");
        }
        return result;
    }


    @GetMapping
    @ApiOperation(value = "获取所有图片列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return null;
    }


    public JsonResultVO saveEntity(ReImage entity) {
        return null;
    }


    public JsonResultVO updateEntityById(Serializable id, ReImage entity) {
        return null;
    }


    public JsonResultVO deleteEntityById(Serializable id) {
        return null;
    }


    @GetMapping("/{id:\\w+}")
    @ApiOperation(value = "根据id获取一个图片", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable(value = "id") Serializable id) {
        return iReImageService.getEntityById(id);
    }

    @GetMapping("/listImagePage")
    @ApiOperation(value = "分页获取图片列表", httpMethod = "GET")
    public JsonResultVO listImagePage(@Validated PageDTO pageDTO) {
        return iReImageService.listImagePage(pageDTO.getPage(), pageDTO.getCount());
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据链接originName和url还有type模糊查询", notes = "根据链接originName和url还有type模糊查询", httpMethod = "POST")
    public JsonResultVO search(ReImageSearchDTO reImageSearchDTO, PageDTO pageDTO) {
        return iReImageService.search(reImageSearchDTO, pageDTO);
    }
}
