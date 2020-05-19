package cn.rootelement.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author ljt
 * @version 1.0.1
 * @date 2020/2/20
 */
@Slf4j
public class JJWTUtil {

    /** jwt过期时间 1天 */
    public final long EXPIRE_TIME = 60 * 60 * 1000;

    /** jwt秘钥 */
    public final String SECRET_KEY = "ROOT_ELEMENT";

    /** jwt默认加密算法HS256 */
    public final SignatureAlgorithm DEFAULT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private JJWTUtil() {}

    private static class Holder {
        private static JJWTUtil instance = new JJWTUtil();
    }

    public static JJWTUtil getInstance() {
        return Holder.instance;
    }

    /**
     * 对秘钥加密
     * @return 加密后的秘钥
     */
    private String generateSecret() {
        // 直接使用Base64进行加密
        return Base64Util.encodeToString(SECRET_KEY.getBytes());
    }

    /**
     * 根据UserDetails生成Claims
     * @param userDetails userDetails
     * @return 返回生成的Claims键值对，当userDetails为null时返回null
     */
    private Map<String, Object> userDetailsToClaims(UserDetails userDetails) {
        if (null == userDetails) {
            log.info("userDetails不能为null");
            return null;
        }
        Map<String, Object> map = new HashMap<>(8);
        map.put("username", userDetails.getUsername());
        map.put("password", userDetails.getPassword());
        map.put("authorities", userDetails.getAuthorities());
        map.put("isAccountNonExpired", userDetails.isAccountNonExpired());
        map.put("isAccountNonLocked", userDetails.isAccountNonLocked());
        map.put("isCredentialsNonExpired", userDetails.isCredentialsNonExpired());
        map.put("isEnabled", userDetails.isEnabled());
        return map;
    }

    /**
     * 生成过期时间
     * @return 过期时间
     */
    private Date generateExpiration() {
        return new Date(System.currentTimeMillis() + EXPIRE_TIME);
    }

    /**
     * 根据claims 和 subject生成token
     * @param claims claims键值对
     * @param subject 用户名
     * @return token
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(generateExpiration())
                .signWith(DEFAULT_SIGNATURE_ALGORITHM, generateSecret());
        return builder.compact();
    }

    /**
     * 直接根据UserDetails对象生成token
     * @param userDetails UserDetails对象
     * @return 生成的token，当userDetails为null时返回null
     */
    public String generateToken(UserDetails userDetails) {
        if (null == userDetails) {
            log.info("userDetails不能为null");
            return null;
        }
        JwtBuilder builder = Jwts.builder()
                .setClaims(userDetailsToClaims(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(generateExpiration())
                .signWith(DEFAULT_SIGNATURE_ALGORITHM, generateSecret());
        return builder.compact();
    }

    /**
     * 从token获取Claims
     * @param token token
     * @return token中的Claims键值对信息，当token不符合格式时返回null
     */
    public Claims getClaimsFromToken(String token) {
        if (StringUtil.isEmpty(token)) {
            log.info("token参数不能为null或者空串");
            return null;
        }
        JwtParser parser = Jwts.parser();
        Claims claims;
        try {
            claims = parser.setSigningKey(generateSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("token解析异常: {}", e.getMessage());
            claims = null;
        }
        return claims;
    }

    /**
     * 从token中获取jwt形式
     * @param token token
     * @return token解析后的jwt，token不符合格式则返回null
     */
    public Jwt getJwtFromToken(String token) {
        if (StringUtil.isEmpty(token)) {
            log.info("token参数不能为null或者空串");
            return null;
        }
        JwtParser parser = Jwts.parser();
        Jwt jwt;
        try {
            jwt = parser.setSigningKey(generateSecret()).parse(token);
        } catch (Exception e) {
            log.info("token解析异常: {}", e.getMessage());
            jwt = null;
        }
        return jwt;
    }

    /**
     * 判断token是否过期
     * @param token token
     * @return 过期返回false, 未过期返回true
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 验证token是否合法
     * @param token token
     * @param userDetails userDetails
     * @return 合法返回true,不合法返回false
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        if (token == null || userDetails == null) {
            return false;
        }
        String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 根据token解析出token中的username
     * @param token 令牌
     * @return username, 如果解析失败那么返回null
     */
    public String getUsernameFromToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        try {
            return claimsFromToken.get("username").toString();
        } catch (Exception e) {
            log.error("从token中解析username失败");
            return null;
        }
    }
}
