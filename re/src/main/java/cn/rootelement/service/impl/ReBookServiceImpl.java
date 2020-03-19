package cn.rootelement.service.impl;

import cn.rootelement.entity.ReBook;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.enumeration.ReEntityRedisKeyEnum;
import cn.rootelement.exception.GlobalToJsonException;
import cn.rootelement.mapper.ReBookMapper;
import cn.rootelement.vo.JsonResultVO;
import cn.rootelement.service.IReBookService;
import cn.rootelement.util.RedisUtil;
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
 * 书籍服务实现类
 *
 * @author ljt
 * @version 1.0
 * @date 2019/11/23
 */
@Service
@Slf4j
public class ReBookServiceImpl extends ServiceImpl<ReBookMapper, ReBook> implements IReBookService {

    private RedisUtil redisUtil;

    @Autowired
    public ReBookServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public JsonResultVO saveEntity(ReBook entity) {
        Optional<ReBook> optionalReBook = Optional.ofNullable(entity);
        optionalReBook.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        boolean save = save(entity);
        String key = ReEntityRedisKeyEnum.RE_BOOK_KEY.getKey()
                .replace(":id", ":" + entity.getId())
                .replace(":name", ":" + entity.getName())
                .replace(":author", ":" + entity.getAuthor())
                .replace(":type", ":" + entity.getType());
        if (save) {
            // 将实体类存储到缓存中去
            redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        Integer bookId = Integer.parseInt(id.toString());
        if (bookId >= 1) {
            // 在数据库中更新
            boolean updateResult = update(new UpdateWrapper<ReBook>().set("`delete`", 0).eq("id", bookId));
            if (updateResult) {
                // 删除缓存中的相关数据
                ReBook reBook = getById(bookId);
                String key = ReEntityRedisKeyEnum.RE_BOOK_KEY.getKey()
                        .replace(":id", ":" + reBook.getId())
                        .replace(":name", ":" + reBook.getName())
                        .replace(":author", ":" + reBook.getAuthor())
                        .replace(":type", ":" + reBook.getType());
                boolean b = redisUtil.hasKey(key);
                if (b) {
                    redisUtil.del(key);
                }
                return JsonResultVO.success(Collections.singletonList(reBook), 1);
            } else {
                throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResultVO updateEntityById(Serializable id, ReBook entity) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        Optional<ReBook> optionalEntity = Optional.ofNullable(entity);
        optionalId.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        optionalEntity.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        Integer bookId = Integer.parseInt(id.toString());
        if (bookId >= 1) {
            boolean updateResult = update(new UpdateWrapper<ReBook>().setEntity(entity).eq("id", bookId));
            if (updateResult) {
                // 更新操作
                String key = ReEntityRedisKeyEnum.RE_BOOK_KEY.getKey()
                        .replace(":id", ":" + entity.getId())
                        .replace(":name", ":" + entity.getName())
                        .replace(":author", ":" + entity.getAuthor())
                        .replace(":type", ":" + entity.getType());
                boolean b = redisUtil.hasKey(key);
                if (b) {
                    redisUtil.set(key, entity, RedisUtil.EXPIRE_TIME_DEFAULT);
                }
                return JsonResultVO.successForMessage("操作成功", 200);
            } else {
                throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResultVO getEntityById(Serializable id) {
        Optional<Serializable> optionalId = Optional.ofNullable(id);
        optionalId.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.PARAM_MISSING_ERROR));
        Integer bookId = Integer.parseInt(id.toString());
        if (bookId >= 1) {
            JsonResultVO jsonResultVO;
            // 如果缓存中存在，那么首先从缓存中获取
            String key = ReEntityRedisKeyEnum.RE_BOOK_KEY.getKey()
                    .replace(":id", ":" + bookId)
                    .replace(":name", ":*")
                    .replace(":author", ":*")
                    .replace(":type", ":*");
            boolean b = redisUtil.hasKeyByPattern(key);
            // 如果存在，那么直接获取
            ReBook reBook;
            if (b) {
                reBook = (ReBook) redisUtil.getByPattern(key);
                if (reBook == null || reBook.getStatus() == 0) {
                    throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
                }
                jsonResultVO = JsonResultVO.success(Collections.singletonList(reBook), 1);
            } else {
                reBook = getById(bookId);
                // 如果不存在，那么返回 找不到资源错误
                if (reBook == null || reBook.getStatus() == 0) {
                    throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
                }
                redisUtil.set(ReEntityRedisKeyEnum.RE_BOOK_KEY.getKey()
                        .replace(":id", ":" + reBook.getId())
                        .replace(":name", ":" + reBook.getName())
                        .replace(":author", ":" + reBook.getAuthor())
                        .replace(":type", ":" + reBook.getType()), reBook, RedisUtil.EXPIRE_TIME_DEFAULT);
                jsonResultVO = JsonResultVO.success(Collections.singletonList(reBook), 1);
            }
            jsonResultVO.setMessage("操作成功");
            return jsonResultVO;
        } else {
            throw new GlobalToJsonException(HttpStatusEnum.PARAM_INVALID_ERROR);
        }
    }

    @Override
    public JsonResultVO listEntityAll() {
        List<ReBook> reBookList = list();
        Optional<List<ReBook>> optionalList = Optional.ofNullable(reBookList);
        optionalList.orElseThrow(() -> new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR));
        optionalList.ifPresent((l) -> l.forEach(reBook -> {
            redisUtil.set(ReEntityRedisKeyEnum.RE_BOOK_KEY.getKey()
                    .replace(":id", ":" + reBook.getId())
                    .replace(":name", ":" + reBook.getName())
                    .replace(":author", ":" + reBook.getAuthor())
                    .replace(":type", ":" + reBook.getType()), reBook, RedisUtil.EXPIRE_TIME_DEFAULT);
        }));
        optionalList.ifPresent(l -> log.info("从数据库中获取所有书籍列表，总条数：" + l.size()));
        JsonResultVO success = JsonResultVO.success(reBookList, reBookList.size());
        success.setMessage("操作成功");
        return success;
    }
}
