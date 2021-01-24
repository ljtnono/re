package cn.ljtnono.re.dto.base;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.IntStream;

/**
 * 列表查询DTO对象基类
 *
 * @author Ling, Jiatong
 * Date: 2020/11/18 23:41
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "列表查询DTO对象基类")
public class BaseListQueryDTO extends BaseDTO {

    /**
     * 排序类型列表 1 表示自然排序（升序） 2 表示降序
     */
    @ApiModelProperty(value = "排序类型列表", notes = "1 表示自然排序（升序） 2 表示降序")
    private List<Integer> sortTypeList;

    /**
     * 前端传递排序字段列表
     */
    @ApiModelProperty(value = "排序字段列表")
    private List<String> sortFieldList;

    /**
     * 允许的排序字段列表，这是一个不可变列表，子类重写此字段
     */
    @ApiModelProperty(value = "允许的排序字段列表", notes = "这是一个不可变列表，子类重写此字段")
    private List<String> sortFieldValueList;

    /**
     * 拼接出来的排序条件
     */
    @ApiModelProperty(value = "拼接出来的排序条件", notes = "前端不可传递此字段")
    private String sortCondition;

    /**
     * 根据排序字段列表和排序方式列表生成最终的排序字符串，方便xml中直接用于拼接
     * 如果传了排序字段，但是没有传排序类型，那么默认为升序排序
     *
     * @throws IllegalArgumentException 当排序字段不对，或者排序类型不对时抛出此异常
     * @author Ling, Jiatong
     */
    public void generateSortCondition() {
        if (!CollectionUtils.isEmpty(sortFieldList)) {
            int len = sortFieldList.size();
            // 默认设置为全部字段升序排序
            if (CollectionUtils.isEmpty(sortTypeList)) {
                sortTypeList = Lists.newArrayList();
                IntStream.range(0, len)
                        // 全部设置为升序排序
                        .forEach(i -> sortTypeList.add(1));
            }
            // 个数不相等
            if (sortTypeList.size() < len) {
                IntStream.range(0, len - sortTypeList.size())
                        .forEach(i -> {
                            // 增加的默认为升序排序
                            sortTypeList.add(1);
                        });
            }
            // 如果排序类型不正确，那么默认设置为升序排序
            for (int i = 0; i < len; i++) {
                if (sortTypeList.get(i) != 1 && sortTypeList.get(i) != 2) {
                    sortTypeList.set(i, 1);
                }
            }
            // 校验排序字段
            if (!getSortFieldValueList().containsAll(sortFieldList)) {
                throw new IllegalArgumentException("排序字段错误");
            }
            // 拼接排序条件
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < len; i++) {
                builder.append(sortFieldList.get(i))
                        .append(" ");
                if (sortTypeList.get(i) == 1) {
                    builder.append("ASC");
                } else {
                    builder.append("DESC");
                }
                if (i != len - 1) {
                    builder.append(",");
                }
            }
            sortCondition = builder.toString();
        }
    }
}
