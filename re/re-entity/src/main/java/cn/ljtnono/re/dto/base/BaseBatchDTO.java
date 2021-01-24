package cn.ljtnono.re.dto.base;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 批量操作DTO基类
 *
 * @author Ling, Jiatong
 * Date: 2021/1/9 21:45
 */
@Data
@ToString
@ApiModel(description = "批量操作DTO基类")
public class BaseBatchDTO<T> {

    /**
     * 批量操作时使用的key
     */
    private List<T> batchKey;

    public BaseBatchDTO() {
        this.batchKey = Lists.newArrayList();
    }
}
