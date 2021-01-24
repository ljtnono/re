package cn.ljtnono.re.dto.system.user;

import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * 用户列表查询DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2020/11/18 23:47
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户列表查询DTO对象")
public class UserListQueryDTO extends BaseListQueryDTO {

    /**
     * 角色Id筛选条件
     */
    @ApiModelProperty("角色id")
    private Integer roleId;

    /**
     * 用户名查询条件
     */
    @ApiModelProperty("查询条件")
    private String searchCondition;

    /**
     * 可选排序字段列表，不可变列表
     */
    @ApiModelProperty(value = "可选排序字段列表", notes = "前端不传递此参数")
    private List<String> sortFieldValueList = Arrays.asList("create_time", "modify_time");

}
