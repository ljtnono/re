package cn.rootelement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 技能查询DTO
 * @author ljt
 * @date 2020/1/14
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReSkillSearchDTO implements Serializable {


    private static final long serialVersionUID = -5089008368605052231L;

    /** 技能名 */
    private String name;

    /** 所有者 */
    private String owner;

}
