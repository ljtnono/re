package cn.ljtnono.re.security.util;

import cn.ljtnono.re.cache.ReUserInfoCache;
import cn.ljtnono.re.common.enumeration.ReRedisKeyEnum;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import cn.ljtnono.re.entity.system.ReUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ling, Jiatong
 * Date: 2020/7/8 8:43 上午
 * Description: jwt工具类
 */
@Component
public class ReJwtUtil {

    @Autowired
    private ReSecurityProperties reSecurityProperties;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 对秘钥进行base64
     *
     * @return base64后的秘钥
     */
    private String generateSecret() {
        return Base64Utils.encodeToString(reSecurityProperties.getTokenSecretKey().getBytes());
    }

    /**
     * 生成过期时间
     *
     * @return 过期时间
     */
    private Date generateExpiration() {
        return new Date(System.currentTimeMillis() + reSecurityProperties.getTokenExpire() * 3600 * 1000);
    }

    /**
     * 根据userId、username生成token
     *
     * @param userId   用户id
     * @param username 用户名
     * @param roleId   角色id
     * @return 生成的token
     */
    public String generateToken(Integer userId, String username, Integer roleId) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("roleId", roleId);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpiration())
                .signWith(SignatureAlgorithm.HS256, generateSecret()).compact();
    }

    /**
     * 判断token是否过期
     * @param token token
     * @throws NullPointerException 当token为null或空串时抛出
     * @see JwtParser#parseClaimsJws(String)  在解析token时会抛出各种异常，具体见此方法
     * @return 过期返回false, 未过期返回true
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 从token获取Claims
     * @param token token
     * @throws NullPointerException 当token为null或空串时抛出
     * @see JwtParser#parseClaimsJws(String)  在解析token时会抛出各种异常，具体见此方法
     * @return token中的Claims键值对信息，当token不符合格式时返回null
     */
    public Claims getClaimsFromToken(String token) {
        JwtParser parser = Jwts.parser();
        return parser.setSigningKey(generateSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证token是否合法
     * @param token token
     * @param reUser 用户对象
     * @see JwtParser#parseClaimsJws(String)  在解析token时会抛出各种异常，具体见此方法
     * @return 合法返回true,不合法返回false
     */
    public boolean validateToken(String token, ReUser reUser) {
        // 缓存校验, 如果存在缓存说明已经登录
        Object cache = redisUtil.get(ReRedisKeyEnum.USER_INFO_KEY.getValue()
                .replace("id", String.valueOf(reUser.getId()))
                .replace("username", reUser.getUsername()));
        if (cache == null) {
            return false;
        }
        ReUserInfoCache reUserInfoCache = (ReUserInfoCache) cache;
        if (!reUserInfoCache.getToken().equals(token)) {
            return false;
        }
        // 校验用户名
        String username = getUsernameFromToken(token);
        if (username != null && username.equals(reUser.getUsername())) {
            isTokenExpired(token);
        }
        return true;
    }

    /**
     * 根据token解析出token中的username
     * @param token 令牌
     * @throws NullPointerException 当token为null或空串时抛出
     * @return username, 如果解析失败那么返回null
     */
    public String getUsernameFromToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return claimsFromToken.get("username").toString();
    }
}
