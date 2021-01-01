package cn.ljtnono.re.excel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 用户数据导出对象
 *
 * @author Ling, Jiatong
 * Date: 2020/11/27 1:16
 */
@Data
@ToString
@ApiModel(description = "用户数据导出对象")
public class UserExcelData {

    /** 用户id */
    @ApiModelProperty("id")
    private Integer id;

    /** 用户名 */
    @ApiModelProperty("用户名")
    private String username;

    /** 用户邮箱 */
    @ApiModelProperty("用户邮箱")
    private String email;

    /** 用户手机号码 */
    @ApiModelProperty("用户手机号码")
    private String phone;

}
