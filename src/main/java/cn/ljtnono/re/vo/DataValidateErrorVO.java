package cn.ljtnono.re.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据校验失败返回的VO对象
 * @author ljt
 * @date 2020/2/12
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class DataValidateErrorVO implements Serializable {

    private static final long serialVersionUID = -6744134059652950524L;

    private String errorMessage;

    private Integer errorCode;

    private Throwable e;

    private Date errorTimeStamp;

}
