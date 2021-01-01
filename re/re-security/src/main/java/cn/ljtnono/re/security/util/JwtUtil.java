package cn.ljtnono.re.security.util;

import cn.ljtnono.re.cache.UserInfoCache;
import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.enumeration.RedisKeyEnum;
import cn.ljtnono.re.common.exception.security.UserPermissionException;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.common.util.redis.RedisUtil;
import cn.ljtnono.re.entity.system.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author Ling, Jiatong
 * Date: 2020/7/8 8:43 上午
 */
@Component
public class JwtUtil {

    private final ReSecurityProperties reSecurityProperties;
    private final RedisUtil redisUtil;
    public JwtUtil(ReSecurityProperties reSecurityProperties, RedisUtil redisUtil) {
        this.reSecurityProperties = reSecurityProperties;
        this.redisUtil = redisUtil;
    }

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
     * @see JwtParser#parseClaimsJws(String)  在解析token时会抛出各种异常，具体见此方法
     * @return 过期返回true, 未过期返回false
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return new Date().after(expiration);
    }

    /**
     * 从token获取Claims
     * @param token token
     * @throws NullPointerException 当token为null或空串时抛出
     * @see JwtParser#parseClaimsJws(String)  在解析token时会抛出各种异常，具体见此方法
     * @return token中的Claims键值对信息
     */
    public Claims getClaimsFromToken(String token) {
        JwtParser parser = Jwts.parser();
        try {
            return parser.setSigningKey(generateSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new UserPermissionException(GlobalErrorEnum.TOKEN_FORMAT_ERROR);
        } catch (SignatureException e) {
            throw new UserPermissionException(GlobalErrorEnum.TOKEN_SIGNATURE_ERROR);
        } catch (ExpiredJwtException e) {
            throw new UserPermissionException(GlobalErrorEnum.TOKEN_EXPIRED_ERROR);
        }
    }

    /**
     * 验证token是否合法
     * @param token token
     * @param user 用户对象
     * @see JwtParser#parseClaimsJws(String)  在解析token时会抛出各种异常，具体见此方法
     * @return 合法返回true,不合法返回false
     */
    public boolean validateToken(String token, User user) {
        // TODO 校验在redis中是否存在，如果存在，那么说明用户已经处于登陆状态，需要比对token与缓存中是否相同，如果不存在，那么说明用户没有登陆
        Object o = redisUtil.get(RedisKeyEnum.USER_INFO_KEY.getValue()
                .replace("id", String.valueOf(user.getId()))
                .replace("username", user.getUsername()));
        if (o == null) {
            return false;
        }
        UserInfoCache userInfoCache = (UserInfoCache) o;
        return userInfoCache.getToken().equalsIgnoreCase(token) && userInfoCache.getUsername().equalsIgnoreCase(user.getUsername());
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
