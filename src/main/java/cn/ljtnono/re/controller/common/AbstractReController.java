package cn.ljtnono.re.controller.common;

import cn.ljtnono.re.pojo.JsonResult;
import cn.ljtnono.re.enumeration.GlobalErrorEnum;
import java.io.Serializable;

/**
 *  controller基类，封装了常用操作
 *  @author 凌家童
 *  @date 2019/10/6
 *  @version 1.0
 *
*/
public abstract class AbstractReController<T> {

    /**
     * 获取实体类的所有列表
     * @return 实体类所有列表
     * 操作成功{request: "success", status: 200, message: "操作成功“, data: {列表}}
     * 操作失败{request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    public JsonResult listEntityAll() { return null;}

    /**
     * 新增单个实体类
     * @param entity 具体的实体类
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    public JsonResult saveEntity(T entity) { return null; }

    /**
     * 根据id更新一个实体类
     * @param id 实体类的id
     * @param entity 需要更新的实体类
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    public JsonResult updateEntityById(Serializable id, T entity) { return null; }

    /**
     * 根据id删除一个实体类
     * @param id 实体类id
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“, data: {删除的实体类}}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    public JsonResult deleteEntityById(Serializable id) { return null; }

    /**
     * 根据id获取一个实体类
     * @param id 实体类id
     * @return 返回操作结果
     * 操作成功返回（如果有附加信息，那么通过fields字段带回，其中特别注意如果data为null，那么不返回)
     * {request: "success", status: 200, message: "操作成功“, data: {实体类}}
     * 操作失败返回
     * {request: "fail", status: 具体错误码{@link GlobalErrorEnum}, message: 具体错误信息{@link GlobalErrorEnum}}
     */
    public JsonResult getEntityById(Serializable id) { return null; }
}
