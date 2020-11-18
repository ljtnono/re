package cn.ljtnono.re.vo.system;

import cn.ljtnono.re.vo.base.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author Ling, Jiatong
 * Date: 2020/11/19 0:36
 * Description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserListVO extends BaseVO {

    /** 用户名 */
    private String username;

    /** 用户手机 */
    private String phone;

    /** 用户邮箱 */
    private String email;

    /** 用户发表的文章数量 */
    private Integer articleNum;

    /** 用户发表的文章的总浏览量 */
    private Integer viewNum;

    /** 用户发表的文章的喜欢总数 */
    private Integer favoriteNum;

    /** 创建时间 */
    private Date createDate;

    /** 最后修改时间 */
    private Date modifyDate;
}