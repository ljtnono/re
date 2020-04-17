package cn.rootelement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 技能存储DTO
 * @author ljt
 * @date 2020/1/19
 * @version 1.0.1
 */
@Data
@NoArgsConstructor
@ToString
public class ReRoleSearchDTO implements Serializable {

    private static final long serialVersionUID = -8647693538365775946L;

    private String name;

    private String description;

}
