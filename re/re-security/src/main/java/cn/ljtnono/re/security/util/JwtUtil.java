package cn.ljtnono.re.security.util;

import cn.ljtnono.re.common.enumeration.GlobalErrorEnum;
import cn.ljtnono.re.common.exception.security.UserPermissionException;
import cn.ljtnono.re.common.properties.ReSecurityProperties;
import cn.ljtnono.re.entity.system.User;
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
public class JwtUtil {

    @Autowired
    private ReSecurityProperties reSecurityProperties;

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
     * @param reUser 用户对象
     * @see JwtParser#parseClaimsJws(String)  在解析token时会抛出各种异常，具体见此方法
     * @return 合法返回true,不合法返回false
     */
    public boolean validateToken(String token, User reUser) {
        // 校验用户名
        String username = getUsernameFromToken(token);
        return username != null && username.equals(reUser.getUsername());
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
