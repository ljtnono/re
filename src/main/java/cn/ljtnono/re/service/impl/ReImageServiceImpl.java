package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReImageSearchDTO;
import cn.ljtnono.re.entity.ReImage;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReImageMapper;
import cn.ljtnono.re.vo.JsonResultVO;
import cn.ljtnono.re.service.IReImageService;
import cn.ljtnono.re.util.RedisUtil;
import cn.ljtnono.re.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 图像类服务实现类
 *
 * @author ljt
 * @version 1.0
 * @date 2019/11/23
 */
@Service
@Slf4j
public class ReImageServiceImpl extends ServiceImpl<ReImageMapper, ReImage> implements IReImageService {

    private RedisUtil redisUtil;

    @Autowired
    public ReImageServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public ReImage getAvatar() {
        // 尝试从缓存获取
        ReImage avatar = (ReImage) redisUtil.getByPattern("re_image:*:avatar:*");
        if (avatar == null) {
            // 尝试从数据库获取
            ReImage one = getOne(new QueryWrapper<ReImage>().eq("origin_name", "avatar"));
            setCache(one);
            return one;
        }
        log.info("从缓存中加载用户头像数据");
        return avatar;
    }

    @Override
    public ReImage getQrCodeWeChat() {
        ReImage qrCodeWeChat = (ReImage) redisUtil.getByPattern("re_image:*:qrcode-wechat:*");
        if (qrCodeWeChat == null) {
            // 尝试从数据库获取
            ReImage one = getOne(new QueryWrapper<ReImage>().eq("origin_name", "qrcode-wechat"));
            setCache(one);
            return one;
        }
        log.info("从缓存中加载用户微信二维码图片");
        return qrCodeWeChat;
    }

    @Override
    public ReImage getQrCodeWeChatSk() {
        ReImage qrCodeWeChatSk = (ReImage) redisUtil.getByPattern("re_image:*:qrcode-wechat-sk:*");
        if (qrCodeWeChatSk == null) {
            // 尝试从数据库获取
            ReImage one = getOne(new QueryWrapper<ReImage>().eq("origin_name", "qrcode-wechat-sk"));
            setCache(one);
            return one;
        }
        log.info("从缓存中加载用户微信扫码支付二维码图片");
        return qrCodeWeChatSk;
    }

    @Override
    public ReImage getQrCodeZfb() {
        ReImage qrCodeZfb = (ReImage) redisUtil.getByPattern("re_image:*:qrcode-zfb:*");
        if (qrCodeZfb == null) {
            // 尝试从数据库获取
            ReImage one = getOne(new QueryWrapper<ReImage>().eq("origin_name", "qrcode-zfb"));
            setCache(one);
            return one;
        }
        log.info("从缓存中加载用户支付宝支付二维码图片");
        return qrCodeZfb;
    }


    @Override
    public JsonResultVO listImagePage(Integer page, Integer count) {
        // 首先从缓存中获取
        String redisKey = ReEntityRedisKeyEnum.RE_IMAGE_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_IMAGE_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页图片数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReImage> pageResult = page(new Page<>(page, count), new QueryWrapper<ReImage>().orderByDesc("modify_time"));
            log.info("从数据库中获取" + page + "页图片数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    @Override
    public JsonResultVO search(ReImageSearchDTO reImageSearchDTO, PageDTO pageDTO) {
        QueryWrapper<ReImage> reImageQueryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(reImageSearchDTO.getOriginName())) {
            reImageQueryWrapper.like("origin_name", reImageSearchDTO.getOriginName());
        }
        if (!StringUtil.isEmpty(reImageSearchDTO.getType())) {
            reImageQueryWrapper.like("type", reImageSearchDTO.getType());
        }
        if (!StringUtil.isEmpty(reImageSearchDTO.getOwner())) {
            reImageQueryWrapper.eq("owner", reImageSearchDTO.getOwner());
        }
        IPage<ReImage> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reImageQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO saveEntity(ReImage entity) {
        boolean save = save(entity);
        if (save) {
            // 将实体类存储到缓存中去
            setCache(entity);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.PARAM_MISSING_ERROR));
        int imageId = Integer.parseInt(id.toString());
        // 在数据库中更新
        boolean updateResult = update(new UpdateWrapper<ReImage>().set("status", 0).eq("id", imageId));
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
            return JsonResultVO.success(Collections.singletonList(reImage), 1);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO updateEntityById(Serializable id, ReImage entity) {
        int imageId = Integer.parseInt(id.toString());
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
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            throw new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public JsonResultVO getEntityById(Serializable id) {
        JsonResultVO jsonResultVO;
        // 如果缓存中存在，那么首先从缓存中获取
        String key = ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                .replace(":id", ":" + id)
                .replace(":originName", ":*")
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
        } else {
            reImage = getById(id);
            // 如果不存在，那么返回 找不到资源错误
            if (reImage == null || reImage.getStatus() == 0) {
                throw new GlobalToJsonException(GlobalErrorEnum.NOT_EXIST_ERROR);
            }
            setCache(reImage);
        }
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reImage), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReImage> reImageList = list();
        Optional<List<ReImage>> optionalList = Optional.ofNullable(reImageList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(GlobalErrorEnum.SYSTEM_ERROR));
        optionalList.ifPresent(this::setCache);
        optionalList.ifPresent(l -> log.info("从数据库中获取所有图片列表，总条数：" + l.size()));
        JsonResultVO success = JsonResultVO.success(reImageList, reImageList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public void setCache(ReImage value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_IMAGE_KEY.getKey()
                .replace(":id", ":" + value.getId())
                .replace(":origin_name", ":" + value.getOriginName())
                .replace(":type", ":" + value.getType())
                .replace(":owner", ":" + value.getOwner()), value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void setCache(List<ReImage> cache) {
        cache.forEach(this::setCache);
    }

    @Override
    public void deleteCacheAll() {

    }
}
