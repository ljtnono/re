package cn.rootelement.es.service;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.entity.ReBlog;
import org.springframework.data.domain.Page;

/**
 * 处理elasticsearch功能的类
 * @author ljt
 * @date 2020/3/17
 * @version 1.0.1
 *
 */
public interface IReBlogEsService {

    /**
     * 新增一个blog记录到elasticsearch中去
     * @param reBlog 新增的blog对象
     * @return 返回要存储的博客对象
     */
    ReBlog save(ReBlog reBlog);

    /**
     * 删除一条记录
     * @param reBlog 要删除的对象
     */
    void delete(ReBlog reBlog);

    /**
     * 获取所有的记录
     * @return Iterable 对象
     */
    Iterable<ReBlog> getAll();

    /**
     * 获取记录总数
     * @return 记录总数
     */
    long count();

    /**
     * 模糊分页查询
     * @param condition 查询字符串
     * @param pageDTO 分页对象
     * @return Page<ReBlog>
     */
    Page<ReBlog> query(String condition, PageDTO pageDTO);

    /**
     * 删除所有记录
     */
    void deleteAll();
}
