package cn.ljtnono.re.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 * 根据数据库表自动生成实体类
 *
 * @author ljt
 * @version 1.1.1
 * @date 2019/10/27
 */
public class EntityGeneratorUtil {

    /**
     * 私有构造函数
     */
    private EntityGeneratorUtil() {}

    /**
     * 实例对象
     */
    private volatile static EntityGeneratorUtil instance = null;

    /**
     * 单例模式获取实例
     *
     * @return 返回工具类实例
     */
    public static EntityGeneratorUtil getInstance() {
        if (instance == null) {
            synchronized (EntityGeneratorUtil.class) {
                if (instance == null) {
                    instance = new EntityGeneratorUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 根据表字段自动生成实体类
     *
     * @param tableName 生成的实体类对应的表名
     * @param entity    生成的实体类对应的class对象
     * @param path      生成类的路径
     */
    public void generatorEntity(JdbcTemplate jdbcTemplate, String tableName, Class<?> entity, String path) {
        if (StringUtil.isEmpty(tableName)) {
            throw new RuntimeException("表名不能为" + tableName);
        }
        // 对单表进行操作
        String sql = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = database() AND table_name = '" + tableName + "'";
        List<Object> query = jdbcTemplate.query(sql, (RowMapper<Object>) ResultSet::getString);
        StringBuilder builder = new StringBuilder();
        String[] s = tableName.split("_");
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].substring(0, 1).toUpperCase() + s[i].substring(1);
        }
        String fileName = Arrays.toString(s);
        // 创建文件
        builder.append("package cn.ljtnono.re.entity;");
        builder.append("\r\n");
    }
}
