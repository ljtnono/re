package cn.ljtnono.re.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 链接类型更新DTO对象
 * @author ljt
 * @date 2020/1/14
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReLinkTypeUpdateDTO implements Serializable {

    private static final long serialVersionUID = -3549125001359469198L;

    /** 链接类型名 */
    @NotNull
    @Length(min = 2, max = 20, message = "链接类型名不能")
    private String name;

}
