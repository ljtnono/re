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

    /** id */
    private Integer id;

    /** 用户名 */
    private String username;

    /** 用户邮箱 */
    private String email;

    /** 用户手机 */
    private String phone;

    /** 可选排序字段列表，不可变列表 */
    private List<String> sortFieldValueList = Arrays.asList("create_date", "username");

}
