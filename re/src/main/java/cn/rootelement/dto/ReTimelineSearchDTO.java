package cn.rootelement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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
public class ReTimelineSearchDTO implements Serializable {

    private static final long serialVersionUID = -1170350593960043035L;

    /** 时间轴内容 */
    private String content;

    /** 时间轴显示日期 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pushDate;
}
