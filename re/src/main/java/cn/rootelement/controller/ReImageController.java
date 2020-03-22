package cn.rootelement.controller;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReImageSearchDTO;
import cn.rootelement.entity.ReImage;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.enumeration.GlobalVariableEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.service.IReImageService;
import cn.rootelement.util.FtpClientUtil;
import cn.rootelement.util.StringUtil;
import cn.rootelement.vo.JsonResultVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @ApiOperation(value = "上传图片接口", notes = "需要具有root权限",httpMethod = "POST")
    public JsonResultVO uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        JsonResultVO jsonResultVO;
        Optional.ofNullable(multipartFile).orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        try {
            // 创建一个image对象
            ReImage reImage = getReImageFromMultipartFile(multipartFile);
            ReImage img = iReImageService.getBaseMapper()
                    .selectOne(new QueryWrapper<ReImage>()
                            .eq("origin_name", reImage.getOriginName())
                            .eq("status", 1));
            if (img != null) {
                return JsonResultVO.forMessage("图片名已存在", HttpStatusEnum.INTERNAL_SERVER_ERROR.getCode());
            }
            boolean b = ftpClientUtil.uploadFile(multipartFile);
            if (b) {

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


    @GetMapping
    @ApiOperation(value = "获取所有图片列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReImageService.listEntityAll();
    }


    @DeleteMapping("/{id:\\w+}")
    @PreAuthorize("hasRole('root')")
    public JsonResultVO deleteEntityById(@PathVariable("id") Serializable id) {
        return iReImageService.deleteEntityById(id);
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