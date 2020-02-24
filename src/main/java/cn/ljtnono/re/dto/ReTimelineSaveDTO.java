package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 时间轴新增DTO对象
 * @author ljt
 * @date 2020/1/15
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReTimelineSaveDTO implements Serializable {

    private static final long serialVersionUID = -7925086379560074305L;

    /** 时间轴内容 */
    @NotNull
    @Length(min = 2, max = 255, message = "内容为2-255个字符")
    private String content;

    /** 时间轴显示日期 */
    @NotNull(message = "发布时间不能为null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "日期格式为yyyy-MM-dd")
    private Date pushDate;
}

