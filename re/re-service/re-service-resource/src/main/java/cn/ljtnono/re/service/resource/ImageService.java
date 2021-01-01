package cn.ljtnono.re.service.resource;

import cn.ljtnono.re.mapper.resource.ImageMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 图片模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/1/1 23:52
 */
@Service
public class ImageService {

    @Resource
    private ImageMapper imageMapper;


    public String upload(MultipartFile multipartFile) {
        return null;
    }
}
