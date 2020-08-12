package cn.ljtnono.re.dto.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ljt
 * Date: 2020/7/13 23:27 下午
 * Description: DTO基类
 */
@Data
@ToString
public class ReBaseDTO implements Serializable {

    private static final long serialVersionUID = -212253609435316598L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 创建时间 */
    private Date createDate;

    /** 最后修改时间 */
    private Date modifyDate;

    /** 分页页数 默认为1 */
    private Integer pageNum = 1;

    /** 每页条数 默认为10 */
    private Integer pageSize = 10;
}
