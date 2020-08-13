package cn.ljtnono.re.dto.system;

import cn.ljtnono.re.dto.base.ReBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljt
 * Date: 2020/8/13 23:18
 * Description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReRoleDTO extends ReBaseDTO implements Serializable {

    private static final long serialVersionUID = 4505176109063156696L;


}
