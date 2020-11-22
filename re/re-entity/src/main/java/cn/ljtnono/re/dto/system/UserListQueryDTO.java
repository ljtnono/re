package cn.ljtnono.re.dto.system;

import cn.ljtnono.re.dto.base.BaseListQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/18 23:47
 * Description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserListQueryDTO extends BaseListQueryDTO {

    /** 角色Id筛选条件 */
    private Integer roleId;

    /** 用户名查询条件 */
    private String searchCondition;

    /** 可选排序字段列表，不可变列表 */
    private List<String> sortFieldValueList = Arrays.asList("create_time", "modify_time");

}
