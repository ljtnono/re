package cn.ljtnono.re.api.controller.resource;

import cn.ljtnono.re.common.vo.JsonResultVO;
import cn.ljtnono.re.dto.resource.image.ImageListQueryDTO;
import cn.ljtnono.re.dto.resource.image.ImageUploadDTO;
import cn.ljtnono.re.service.resource.ImageService;
import cn.ljtnono.re.vo.resource.image.ImageListVO;
import cn.ljtnono.re.vo.resource.image.ImageUploadVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;

/**
 * 图片模块接口
 *
 * @author Ling, Jiatong
 * Date: 2021/1/25 0:14
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/rs/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 上传图片
     *
     * @param dto 上传图片DTO对象
     * @return 上传图片返回VO对象
     * @author Ling, Jiatong
     */
    @PostMapping("/uploadImage")
    @PreAuthorize("hasAnyAuthority('rs:image:upload')")
    @ApiOperation(value = "上传图片", httpMethod = "POST")
    public JsonResultVO<ImageUploadVO> uploadImage(ImageUploadDTO dto) {
        log.info("[re-system -> ImageController -> uploadImage()] 上传图片，参数：{}", dto);
        return JsonResultVO.success(imageService.uploadImage(dto));
    }

    /**
     * 分页获取图片列表
     *
     * @param dto 图片列表查询DTO对象
     * @return 图片列表VO对象分页对象
     * @author Ling, Jiatong
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('rs:image:view')")
    @ApiOperation(value = "分页获取图片列表", httpMethod = "GET")
    public JsonResultVO<IPage<ImageListVO>> getList(ImageListQueryDTO dto) {
        log.info("[re-system -> ImageController -> getList()] 获取图片列表，参数：{}", dto);
        return JsonResultVO.success(imageService.getList(dto));
    }


}
