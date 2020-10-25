package cn.ljtnono.re.cache;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Ling, Jiatong
 * Date: 2020/10/6 21:17
 * Description: 用户在redis中的缓存对象
 */
@Data
@ToString
public class UserInfoCache {

    /** 用户id */
    private Integer userId;

    /** 用户名 */
    private String username;

    /** 角色id */
    private Integer roleId;

    /** token */
    private String token;

    /** 登陆时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;
}
