package cn.ljtnono.re.service.impl;

import cn.ljtnono.re.dto.PageDTO;
import cn.ljtnono.re.dto.ReUserSearchDTO;
import cn.ljtnono.re.entity.ReRole;
import cn.ljtnono.re.entity.ReUser;
import cn.ljtnono.re.enumeration.HttpStatusEnum;
import cn.ljtnono.re.enumeration.ReEntityRedisKeyEnum;
import cn.ljtnono.re.exception.GlobalToJsonException;
import cn.ljtnono.re.mapper.ReUserMapper;
import cn.ljtnono.re.vo.JsonResultVO;
import cn.ljtnono.re.service.IReUserService;
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

/**
 * 用户service类的实现类
 * @author ljt
 * @date 2019/11/15
 * @version 1.0.1
 *
 */
@Service
@Slf4j
public class ReUserServiceImpl extends ServiceImpl<ReUserMapper, ReUser> implements IReUserService {

    private RedisUtil redisUtil;

    @Autowired
    public ReUserServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public List<ReRole> listRoleByUserId(Integer userId) {
        return getBaseMapper().listRoleByUserId(userId);
    }


    @Override
    public JsonResultVO listUserPage(Integer page, Integer count) {
        String redisKey = ReEntityRedisKeyEnum.RE_USER_PAGE_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        String totalRedisKey = ReEntityRedisKeyEnum.RE_USER_PAGE_TOTAL_KEY.getKey()
                .replace(":page", ":" + page)
                .replace(":count", ":" + count);
        // 首先从缓存中拿 这里lGet如果查询不到，会自动返回空集合
        List<?> objects = redisUtil.lGet(redisKey, 0, -1);
        if (!objects.isEmpty()) {
            log.info("从缓存中获取" + page + "页用户数据，每页获取" + count + "条");
            String getByPattern = (String) redisUtil.getByPattern(totalRedisKey);
            return JsonResultVO.success((Collection<?>) objects.get(0), ((Collection<?>) objects.get(0)).size()).addField("totalPages", getByPattern.split("_")[0]).addField("totalCount", getByPattern.split("_")[1]);
        } else {
            // 按照时间降序排列
            IPage<ReUser> pageResult = page(new Page<>(page, count), new QueryWrapper<ReUser>().orderByDesc("modify_time"));
            log.info("从数据库中获取" + page + "页用户数据，每页获取" + count + "条");
            redisUtil.lSet(redisKey, pageResult.getRecords(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            redisUtil.set(totalRedisKey, pageResult.getPages() + "_" + pageResult.getTotal(), RedisUtil.EXPIRE_TIME_PAGE_QUERY);
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        }
    }

    /**
     * 恢复删除的用户
     *
     * @param id 用户id
     * @return JsonResult对象
     */
    @Override
    public JsonResultVO restore(Serializable id) {
        int userId = Integer.parseInt(id.toString());
        // 更新状态
        boolean update = update(new UpdateWrapper<ReUser>().set("status", 1).eq("id", userId));
        // 更新数据库中相关表的记录
        getBaseMapper().restoreUserRole(userId);
        if (update) {
            // 删除缓存中的相关数据
            ReUser user = getById(userId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(user), 1);
        } else {
            log.error("恢复用户失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO search(ReUserSearchDTO reUserSearchDTO, PageDTO pageDTO) {
        QueryWrapper<ReUser> reUserQueryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(reUserSearchDTO.getUsername())) {
            reUserQueryWrapper.like("username", reUserSearchDTO.getUsername());
        }
        if (!StringUtil.isEmpty(reUserSearchDTO.getTel())) {
            reUserQueryWrapper.like("tel", reUserSearchDTO.getUsername());
        }
        if (!StringUtil.isEmpty(reUserSearchDTO.getEmail())) {
            reUserQueryWrapper.like("email", reUserSearchDTO.getUsername());
        }
        reUserQueryWrapper.orderByDesc("modify_time");
        IPage<ReUser> pageResult = page(new Page<>(pageDTO.getPage(), pageDTO.getCount()), reUserQueryWrapper);
        if (pageResult != null) {
            return JsonResultVO.success(pageResult.getRecords(), pageResult.getRecords().size()).addField("totalPages", pageResult.getPages()).addField("totalCount", pageResult.getTotal());
        } else {
            log.error("查询用户失败, {}, {}", reUserSearchDTO, pageDTO);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO saveEntity(ReUser entity) {
        boolean save = save(entity);
        if (save) {
            // 将实体类存储到缓存中去
            setCache(entity);
            return JsonResultVO.successForMessage("操作成功！", 200);
        } else {
            log.error("新增用户失败, {}", entity);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JsonResultVO deleteEntityById(Serializable id) {
        int userId = Integer.parseInt(id.toString());
        // 在数据库中更新
        boolean updateResult = update(new UpdateWrapper<ReUser>().set("status", 0).eq("id", userId));
        // 删除re_user_role表中的记录, 这里因为用户总有角色，所以不需要判断是否有角色
        getBaseMapper().deleteUserRole(userId);
        if (updateResult) {
            // 删除缓存中的相关数据
            ReUser reUser = getById(userId);
            deleteCacheAll();
            return JsonResultVO.success(Collections.singletonList(reUser), 1);
        } else {
            log.error("删除用户失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO updateEntityById(Serializable id, ReUser entity) {
        int userId = Integer.parseInt(id.toString());
        boolean updateResult = update(entity, new UpdateWrapper<ReUser>().eq("id", userId));
        if (updateResult) {
            // 删除所有缓存
            deleteCacheAll();
            return JsonResultVO.successForMessage("操作成功", 200);
        } else {
            log.error("更新用户失败, id = {}", id);
            throw new GlobalToJsonException(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public JsonResultVO getEntityById(Serializable id) {
        int userId = Integer.parseInt(id.toString());
        JsonResultVO jsonResultVO;
        // 如果缓存中存在，那么首先从缓存中获取
        String key = ReEntityRedisKeyEnum.RE_USER_KEY.getKey()
                .replace(":id", ":" + userId)
                .replace(":username", ":*")
                .replace(":qq", ":*")
                .replace(":tel", ":*");
        boolean b = redisUtil.hasKeyByPattern(key);
        // 如果存在，那么直接获取
        ReUser reUser;
        if (b) {
            reUser = (ReUser) redisUtil.getByPattern(key);
            if (reUser == null || reUser.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
        } else {
            reUser = getById(userId);
            // 如果不存在，那么返回 找不到资源错误
            if (reUser == null || reUser.getStatus() == 0) {
                throw new GlobalToJsonException(HttpStatusEnum.NOT_FOUND);
            }
            setCache(reUser);
        }
        jsonResultVO = JsonResultVO.success(Collections.singletonList(reUser), 1);
        jsonResultVO.setMessage("操作成功");
        return jsonResultVO;
    }


    @Override
    public JsonResultVO listEntityAll() {
        List<ReUser> reUserList = list(new QueryWrapper<ReUser>().orderByDesc("modify_time"));
        setCache(reUserList);
        log.info("从数据库中获取所有用户列表，总条数：" + reUserList.size());
        JsonResultVO success = JsonResultVO.success(reUserList, reUserList.size());
        success.setMessage("操作成功");
        return success;
    }

    @Override
    public void setCache(ReUser value) {
        redisUtil.set(ReEntityRedisKeyEnum.RE_USER_KEY.getKey()
                        .replace(":id", ":" + value.getId())
                        .replace(":username", ":" + value.getUsername())
                        .replace(":qq", ":" + value.getQq())
                        .replace(":tel", ":" + value.getTel())
                , value, RedisUtil.EXPIRE_TIME_DEFAULT);
    }

    @Override
    public void setCache(List<ReUser> cache) {
        cache.forEach(this::setCache);
    }

    @Override
    public void deleteCacheAll() {
        redisUtil.deleteByPattern("re_user:*");
        redisUtil.deleteByPattern("re_user_page:*");
        redisUtil.deleteByPattern("re_user_page_total:*");
    }
}
