package cn.ljtnono.re.entity.sys;

import cn.ljtnono.re.entity.base.ReBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ljt
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sys_user")
public class ReUser extends ReBaseEntity implements Serializable {

    private static final long serialVersionUID = 4057606458753697276L;


}
