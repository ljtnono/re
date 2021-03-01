package cn.ljtnono.re.service.system;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.ParamException;
import cn.ljtnono.re.entity.system.Config;
import cn.ljtnono.re.mapper.system.ConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 全局配置模块service层
 *
 * @author Ling, Jiatong
 * Date: 2020/11/6 23:31
 */
@Service
public class ConfigService {

    @Resource
    private ConfigMapper configMapper;
    //*********************************** 接口方法 ***********************************//

    /**
     * 根据id获取配置项
     *
     * @param key 配置项key
     * @return Config 配置实体类，如果不存在返回{@literal null}
     * @author Ling, Jiatong
     */
    public Config getConfigByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new ParamException(GlobalErrorEnum.CONFIG_ID_NULL_ERROR);
        }
        return configMapper.selectOne(new LambdaQueryWrapper<Config>()
                .eq(Config::getKey, key));
    }

    //*********************************** 私有方法 ***********************************//

    //*********************************** 公共方法 ***********************************//

    //*********************************** 其他方法 ***********************************//

}
