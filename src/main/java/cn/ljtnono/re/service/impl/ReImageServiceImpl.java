package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.entity.ReImage;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReImageMapper;
import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.service.IReImageService;
import cn.ljtnono.re.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 图像类服务实现类
 * @author ljt
 * @date 2019/11/23
 * @version 1.0
 */
@Service
@Slf4j
public class ReImageServiceImpl extends ServiceImpl<ReImageMapper, ReImage> implements IReImageService {

    private final RedisUtil redisUtil;

    @Autowired
    public ReImageServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 获取博主头像
     *
     * @return 博主头像封装的ReImage类
     */
    @Override
    public ReImage getAvatar() {
        // 尝试从缓存获取
        ReImage avatar = (ReImage) redisUtil.getByPattern("re_image:*:avatar:*");
        if (avatar == null) {
            // 尝试从数据库获取
            ReImage one = getOne(new QueryWrapper<ReImage>().eq("origin_name", "avatar"));
            redisUtil.set(ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                    .replace("id", one.getId())
                    .replace("origin_name", one.getOriginName())
                    .replace("type", one.getType())
                    .replace("owner", one.getOwner()), one, RedisUtil.EXPIRE_TIME_DEFAULT);
            return one;
        }
        log.info("从缓存中加载用户头像数据");
        return avatar;
    }

    /**
     * 获取加我微信的二维码图片
     *
     * @return 加我微信的二维码头像图片
     */
    @Override
    public ReImage getQrCodeWeChat() {
        ReImage qrCodeWeChat = (ReImage) redisUtil.getByPattern("re_image:*:qrcode-wechat:*");
        if (qrCodeWeChat == null) {
            // 尝试从数据库获取
            ReImage one = getOne(new QueryWrapper<ReImage>().eq("origin_name", "qrcode-wechat"));
            redisUtil.set(ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                    .replace("id", one.getId())
                    .replace("origin_name", one.getOriginName())
                    .replace("type", one.getType())
                    .replace("owner", one.getOwner()), one, RedisUtil.EXPIRE_TIME_DEFAULT);
            return one;
        }
        log.info("从缓存中加载用户微信二维码图片");
        return qrCodeWeChat;
    }

    /**
     * 获取微信扫码支付的二维码图片
     *
     * @return 扫码支付二维码
     */
    @Override
    public ReImage getQrCodeWeChatSk() {
        ReImage qrCodeWeChatSk = (ReImage) redisUtil.getByPattern("re_image:*:qrcode-wechat-sk:*");
        if (qrCodeWeChatSk == null) {
            // 尝试从数据库获取
            ReImage one = getOne(new QueryWrapper<ReImage>().eq("origin_name", "qrcode-wechat-sk"));
            redisUtil.set(ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                    .replace("id", one.getId())
                    .replace("origin_name", one.getOriginName())
                    .replace("type", one.getType())
                    .replace("owner", one.getOwner()), one, RedisUtil.EXPIRE_TIME_DEFAULT);
            return one;
        }
        log.info("从缓存中加载用户微信扫码支付二维码图片");
        return qrCodeWeChatSk;
    }

    /**
     * 获取支付宝扫码支付图片
     *
     * @return 支付宝扫码支付的二维码
     */
    @Override
    public ReImage getQrCodeZfb() {
        ReImage qrCodeZfb = (ReImage) redisUtil.getByPattern("re_image:*:qrcode-zfb:*");
        if (qrCodeZfb == null) {
            // 尝试从数据库获取
            ReImage one = getOne(new QueryWrapper<ReImage>().eq("origin_name", "qrcode-zfb"));
            redisUtil.set(ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                    .replace("id", one.getId())
                    .replace("origin_name", one.getOriginName())
                    .replace("type", one.getType())
                    .replace("owner", one.getOwner()), one, RedisUtil.EXPIRE_TIME_DEFAULT);
            return one;
        }
        log.info("从缓存中加载用户支付宝支付二维码图片");
        return qrCodeZfb;
    }

    /**
     * 新增单个实体类
     *
     * @param entity 具体的实体类
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult saveEntity(ReImage entity) {
        Optional<ReImage> optionalReBook = Optional.ofNullable(entity);
        optionalReBook.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":origin_name", ":" + entity.getOriginName())
                .replace(":type", ":" + entity.getType())
                .replace(":owner", ":" + entity.getOwner());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            return JsonResult.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 根据id删除一个实体类
     *
     * @param id 实体类id
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“, data: {删除的实体类}}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        Integer imageId = Integer.parseInt(id.toString());
        if (imageId >= 1) {
            // 在数据库中更新
            boolean updateResult = update(new UpdateWrapper<ReImage>().set("`delete`", 0).eq("id", imageId));
            if (updateResult) {
                // 删除缓存中的相关数据
                ReImage reImage = getById(imageId);
                String key = ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                        .replace(":id", ":" + reImage.getId())
                        .replace(":origin_name", ":" + reImage.getOriginName())
                        .replace(":type", ":" + reImage.getType())
                        .replace(":owner", ":" + reImage.getOwner());
                boolean b = redisUtil.hasKey(key);
                if (b) {
                    redisUtil.del(key);
                }
                return JsonResult.success(Collections.singletonList(reImage), 1);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    /**
     * 根据id更新一个实体类
     *
     * @param id     实体类的id
     * @param entity 要更新的实体类
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult updateEntityById(Serializable id, ReImage entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReImage> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        Integer imageId = Integer.parseInt(id.toString());
        if (imageId >= 1) {
            boolean updateResult = update(new UpdateWrapper<ReImage>().setEntity(entity).eq("id", imageId));
            if (updateResult) {
                // 更新操作
                String key = ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                        .replace(":id", ":" + entity.getId())
                        .replace(":origin_name", ":" + entity.getOriginName())
                        .replace(":type", ":" + entity.getType())
                        .replace(":owner", ":" + entity.getOwner());
                boolean b = redisUtil.hasKey(key);
                if (b) {
                    redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
                }
                return JsonResult.successForMessage("操作成功", 200);
            } else {
                throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
            }
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    /**
     * 根据id获取一个实体类
     *
     * @param id 实体类id
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“, data: {实体类}}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult getEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        Integer imageId = Integer.parseInt(id.toString());
        if (imageId >= 1) {
            JsonResult jsonResult;
            // 如果缓存中存在，那么首先从缓存中获取
            String key = ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                    .replace(":id", ":" + imageId)
                    .replace(":origin_name", ":*")
                    .replace(":type", ":*")
                    .replace(":owner", ":*");
            boolean b = redisUtil.hasKeyByPattern(key);
            // 如果存在，那么直接获取
            ReImage reImage;
            if (b) {
                reImage = (ReImage) redisUtil.getByPattern(key);
                if (reImage == null || reImage.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                jsonResult = JsonResult.success(Collections.singletonList(reImage), 1);
            } else {
                reImage = getById(imageId);
                // 如果不存在，那么返回 找不到资源错误
                if (reImage == null || reImage.getStatus() == 0) {
                    throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                        .replace(":id", ":" + reImage.getId())
                        .replace(":origin_name", ":" + reImage.getOriginName())
                        .replace(":type", ":" + reImage.getType())
                        .replace(":owner", ":" + reImage.getOwner()), reImage, RedisUtil.EXPIRE_TIME_DEFAULT);
                jsonResult = JsonResult.success(Collections.singletonList(reImage), 1);
            }
            jsonResult.setMessage("操作成功");
            return jsonResult;
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.PARAM_INVALID_ERROR);
        }
    }

    /**
     * 获取实体类的所有列表
     *
     * @return 实体类所有列表
     * 操作成功{request: "success", status: 200, message: "操作成功“, data: {列表}}
     * 操作失败{request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    @Override
    public JsonResult listEntityAll() {
        List<ReImage> reImageList = list();
        Optional<List<ReImage>> optionalList = Optional.ofNullable(reImageList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reImage -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_CONFIG_KEY.getKey()
                    .replace(":id", ":" + reImage.getId())
                    .replace(":origin_name", ":" + reImage.getOriginName())
                    .replace(":type", ":" + reImage.getType())
                    .replace(":owner", ":" + reImage.getOwner()), reImage, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有图片列表，总条数：" + l.size()));
        JsonResult success = JsonResult.success(reImageList, reImageList.size());
        success.setMessage("操作成功");
        return success;
    }
}
