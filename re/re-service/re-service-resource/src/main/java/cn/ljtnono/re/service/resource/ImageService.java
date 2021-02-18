package cn.ljtnono.re.service.resource;

import cn.ljtnono.re.common.constant.StaticFolderConstant;
import cn.ljtnono.re.common.constant.resource.ImageCompressTypeEnum;
import cn.ljtnono.re.common.constant.resource.ImageConstant;
import cn.ljtnono.re.common.constant.resource.ImageTypeEnum;
import cn.ljtnono.re.common.enumeration.EntityConstantEnum;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.common.exception.businese.BusinessException;
import cn.ljtnono.re.common.exception.system.DataBaseException;
import cn.ljtnono.re.common.properties.ReProperties;
import cn.ljtnono.re.common.util.DateUtil;
import cn.ljtnono.re.common.util.FileUtil;
import cn.ljtnono.re.common.util.RandomUtil;
import cn.ljtnono.re.common.util.SpringBeanUtil;
import cn.ljtnono.re.dto.resource.image.ImageListQueryDTO;
import cn.ljtnono.re.dto.resource.image.ImageUploadBatchDTO;
import cn.ljtnono.re.dto.resource.image.ImageUploadDTO;
import cn.ljtnono.re.entity.resource.Image;
import cn.ljtnono.re.mapper.resource.ImageMapper;
import cn.ljtnono.re.vo.resource.image.ImageListVO;
import cn.ljtnono.re.vo.resource.image.ImageUploadVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 图片模块service层
 *
 * @author Ling, Jiatong
 * Date: 2021/1/1 23:52
 */
@Slf4j
@Service
public class ImageService {

    @Resource
    private ImageMapper imageMapper;
    @Autowired
    private ReProperties reProperties;
    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${server.port}")
    private Integer port;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //*********************************** 接口方法 ***********************************//

    /**
     * 分页获取图片列表
     *
     * @param dto 图片分页查询DTO对象
     * @return 图片列表VO对象分页对象
     * @author Ling, Jiatong
     */
    public IPage<ImageListVO> getList(ImageListQueryDTO dto) {
        try {
            dto.generateSortCondition();
            // 默认按照上传时间降序
            if (!dto.getSortFieldList().contains("create_time")) {
                dto.getSortFieldList().add("create_time");
                dto.getSortTypeList().add(2);
            }
        } catch (IllegalArgumentException e) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
        return imageMapper.getList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto);
    }

    /**
     * 上传图片
     * 单个图片大小不得超过10M
     *
     * @param dto 上传图片DTO对象
     * @return 上传图片返回VO对象
     * @author Ling, Jiatong
     */
    public ImageUploadVO uploadImage(ImageUploadDTO dto){
        // 校验上传文件参数
        checkUploadParam(dto);
        MultipartFile file = dto.getFile();
        long size = file.getSize();
        // 限制图片大小
        if (size > ImageConstant.MAX_SIZE_PER_IMAGE) {
            // 图片大小过大
            throw new BusinessException(GlobalErrorEnum.IMAGE_TOO_LARGE_ERROR);
        }
        // 限制图片种类
        String originalFilename = file.getOriginalFilename();
        // 校验图片名，不能是有问题的文件名
        if (StringUtils.isEmpty(originalFilename) || !FileUtil.getInstance().isValidFileName(originalFilename)) {
            throw new BusinessException(GlobalErrorEnum.IMAGE_NAME_ERROR);
        }
        // 校验图片后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (!ImageConstant.ALLOWED_SUFFIX.contains(suffix)) {
            throw new BusinessException(GlobalErrorEnum.IMAGE_SUFFIX_NOT_SUPPORT_ERROR);
        }
        // 验证图片类型
        Integer type = dto.getType();
        // 重新生成随机图片名，保证不会出现重名图片
        String imageId = RandomUtil.getInstance().generateSimpleUUID();
        String imageName = imageId + "." + suffix;
        String savePath;
        String url;
        if (ImageTypeEnum.BLOG_IMAGE.getCode().equals(type)) {
            savePath = reProperties.getStaticFileBasePath()
                    + File.separator
                    + StaticFolderConstant.FOLDER_BLOG_IMAGE
                    + File.separator
                    + imageName;
            url = ImageConstant.ACCESS_HOST + ":" + port + contextPath + "/" + "static" + "/" + StaticFolderConstant.FOLDER_BLOG_IMAGE + "/" + imageName;
        } else {
            savePath = reProperties.getStaticFileBasePath()
                    + File.separator
                    + StaticFolderConstant.FOLDER_IMAGE
                    + File.separator
                    + imageName;
            url = ImageConstant.ACCESS_HOST + ":" + port + contextPath + "/" + "static" + "/" + StaticFolderConstant.FOLDER_IMAGE + "/" + imageName;
        }
        // 存储文件
        try {
            File saveFile = new File(savePath);
            if (!saveFile.exists()) {
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
            }
            file.transferTo(saveFile);
            // 计算文件MD5值，如果相同，那么校验成功，如果不相同，那么抛出上传失败异常，并且删除该图片
            String md5 = DigestUtils.md5DigestAsHex(new FileInputStream(saveFile));
            if (!dto.getMd5().equalsIgnoreCase(md5)) {
                FileSystemUtils.deleteRecursively(new File(savePath));
                throw new BusinessException(GlobalErrorEnum.IMAGE_MD5_VALIDATE_FAILED_ERROR);
            }
        } catch (IOException e) {
            ImageUploadVO imageUploadVO = new ImageUploadVO();
            imageUploadVO.setResult(ImageConstant.UPLOAD_FAILED_CODE);
            imageUploadVO.setMessage(e.getMessage());
            return imageUploadVO;
        }
        // 图片数据入库
        Image image = new Image();
        image.setDeleted(EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue());
        image.setCreateTime(new Date());
        image.setModifyTime(new Date());
        image.setMd5(dto.getMd5());
        image.setSuffix(suffix);
        image.setSize(file.getSize());
        image.setType(dto.getType());
        image.setOriginName(file.getOriginalFilename());
        image.setImageId(imageId);
        image.setSavePath(savePath);
        image.setUrl(url);
        if (imageMapper.insert(image) <= 0) {
            throw new DataBaseException(GlobalErrorEnum.DATABASE_OPERATION_ERROR);
        }
        ImageUploadVO vo = new ImageUploadVO();
        vo.setResult(ImageConstant.UPLOAD_SUCCESS_CODE);
        vo.setImageId(imageId);
        vo.setSavePath(savePath);
        vo.setUrl(url);
        return vo;
    }

    /**
     * 图片批量上传
     *
     * @param dto 图片批量删除DTO对象
     * @author Ling, Jiatong
     */
    public void uploadImageBatch(ImageUploadBatchDTO dto) {

    }

    /**
     * 批量下载图片
     *
     * @param idList 图片id列表
     * @param compressType 压缩类型
     * @author Ling, Jiatong
     */
    public void downloadImageBatch(List<Integer> idList, Integer compressType) throws IOException {
        HttpServletResponse response = SpringBeanUtil.getCurrentRes();
        if (CollectionUtils.isEmpty(idList)) {
            response.setContentType("application/octet-stream");
            // 写回一个0字节，前端可进行判断
            response.getOutputStream().write(0);
            return;
        }
        // 这里不用校验是否为空，默认入库数据不可能为空
        List<String> savePathList = getSavePath(idList);
        List<File> fileList = savePathList.stream()
                .map(File::new)
                .collect(Collectors.toList());
        if (ImageCompressTypeEnum.ZIP.getCode().equals(compressType)) {
            // 将文件打包成zip格式
            String fileName = DateUtil.formatDate(new Date(), DateUtil.DateStyleEnum.yyyyMMddHHmmss) + ".zip";
            File zipFile = new File(reProperties.getStaticFileBasePath() + "tmp" + File.separator + fileName);
            FileUtil.getInstance().zipFiles(fileList, zipFile);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");
            FileUtil.getInstance().downLoadFile(response.getOutputStream(), zipFile);
        } else if (ImageCompressTypeEnum.TAR_GZ.getCode().equals(compressType)) {
            // 将文件打包成tar.gz格式
            String fileName = DateUtil.formatDate(new Date(), DateUtil.DateStyleEnum.yyyyMMddHHmmss) + ".tar.gz";
            File tarFile = new File(reProperties.getStaticFileBasePath() + "tmp" + File.separator + fileName);
            File file = FileUtil.getInstance().tarGzFiles(fileList, tarFile);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");
            FileUtil.getInstance().downLoadFile(response.getOutputStream(), file);
        } else {
            // 将文件打包成rar格式
        }
    }

    /**
     * 根据存储路径校验完整性
     * 如果是逻辑删除的图片会返回false
     *
     * @param savePath 文件存储路径
     * @return 图片完整返回true，图片不完整或者图片被逻辑删除或者图片不存在返回false
     * @author Ling, Jiatong
     */
    public boolean checkCompleteness(String savePath) throws IOException {
        if (StringUtils.isEmpty(savePath)) {
            return false;
        }
        File file = new File(savePath);
        if (!file.exists()) {
            return false;
        }
        String savedMd5 = getMd5(savePath);
        if (StringUtils.isEmpty(savedMd5)) {
            return false;
        }
        String md5 = DigestUtils.md5DigestAsHex(new FileInputStream(file));
        return savedMd5.equalsIgnoreCase(md5);
    }

    /**
     * 批量逻辑删除图片
     *
     * @param idList 图片id列表
     * @author Ling, Jiatong
     */
    public void logicDeleteBatch(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        imageMapper.update(null, new LambdaUpdateWrapper<Image>()
                .in(Image::getId, idList));
    }

    /**
     * 批量物理删除图片
     *
     * @param idList 图片id列表
     * @author Ling, Jiatong
     */
    public void physicDeleteBatch(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<String> savePathList = getSavePath(idList);
        // 删除表数据
        imageMapper.delete(new LambdaQueryWrapper<Image>().in(Image::getId, idList));
        // 删除图片文件
        savePathList.forEach(savePath -> FileSystemUtils.deleteRecursively(new File(savePath)));
    }

    //*********************************** 私有方法 ***********************************//

    /**
     * 校验上传文件参数
     * 不校验图片类型
     *
     * @param dto 图片上传DTO对象
     * @author Ling, Jiatong
     */
    private void checkUploadParam(ImageUploadDTO dto) {
        Optional.ofNullable(dto)
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        Optional.ofNullable(dto.getFile())
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        Optional.ofNullable(dto.getType())
                .orElseThrow(() -> new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR));
        // 校验md5
        if (StringUtils.isEmpty(dto.getMd5())) {
            throw new ParamException(GlobalErrorEnum.REQUEST_PARAM_ERROR);
        }
    }


    //*********************************** 公共方法 ***********************************//

    /**
     * 获取图片存储路径列表
     *
     * 不包含逻辑删除图片
     *
     * @param idList 图片id列表
     * @return 图片存储路径列表
     * @author Ling, Jiatong
     */
    public List<String> getSavePath(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Lists.newArrayList();
        }
        return imageMapper.selectList(new LambdaQueryWrapper<Image>()
                .select(Image::getSavePath)
                .in(Image::getId, idList)
                .eq(Image::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue()))
                .stream()
                .filter(Objects::nonNull)
                .map(Image::getSavePath)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据图片存储路径获取数据库中存储的md5值
     * 不包含逻辑删除过的图片
     *
     * @param savePath 图片存储路径
     * @return 图片md5值
     * @author Ling, Jiatong
     */
    public String getMd5(String savePath) {
        if (StringUtils.isEmpty(savePath)) {
            return null;
        }
        return imageMapper.selectOne(new LambdaQueryWrapper<Image>()
                .eq(Image::getDeleted, EntityConstantEnum.ENTITY_IS_DELETED_NOT_DELETED.getValue())
                .eq(Image::getSavePath, savePath))
                .getMd5();
    }

    //*********************************** 其他方法 ***********************************//

}
