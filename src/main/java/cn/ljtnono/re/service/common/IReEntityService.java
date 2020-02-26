package cn.ljtnono.re.service.common;

import cn.ljtnono.re.enumeration.HttpStatusEnum;
import cn.ljtnono.re.vo.JsonResultVO;

import java.io.Serializable;

/**
 * service父类
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.2
 */
public interface IReEntityService <T> extends IReEntityCacheManager <T> {

    /**
     * 新增单个实体类
     * @param entity 具体的实体类
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link HttpStatusEnum}, message: 具体错误信息{@link HttpStatusEnum}}
     */
    JsonResultVO saveEntity(T entity);

    /**
     * 根据id删除一个实体类
     * @param id 实体类id
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“, data: {删除的实体类}}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link HttpStatusEnum}, message: 具体错误信息{@link HttpStatusEnum}}
     */
    JsonResultVO deleteEntityById(Serializable id);

    /**
     * 根据id更新一个实体类
     * @param id 实体类的id
     * @param entity 要更新的实体类
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link HttpStatusEnum}, message: 具体错误信息{@link HttpStatusEnum}}
     */
    JsonResultVO updateEntityById(Serializable id, T entity);

    /**
     * 根据id获取一个实体类
     * @param id 实体类id
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“, data: {实体类}}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link HttpStatusEnum}, message: 具体错误信息{@link HttpStatusEnum}}
     */
    JsonResultVO getEntityById(Serializable id);

    /**
     * 获取实体类的所有列表, 默认根据最后修改时间降序
     * @return 实体类所有列表
     * 操作成功{request: "success", status: 200, message: "操作成功“, data: {列表}}
     * 操作失败{request: "fail", status: 具体错误码{@link HttpStatusEnum}, message: 具体错误信息{@link HttpStatusEnum}}
     */
    JsonResultVO listEntityAll();
}
