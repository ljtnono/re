package cn.ljtnono.re.security.util;

import cn.ljtnono.re.entity.system.ReUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author ljt
 * Date: 2020/7/8 8:43 上午
 * Description:
 */
@Component
@Slf4j
@ConfigurationProperties(prefix = "jwt")
public class ReJwtUtil {

    /**
     * jwt过期时间 2小时
     */
    @Value("${jwt.expire}")
    public long expire;

    /**
     * jwt秘钥
     */
    @Value("${jwt.secretKey}")
    public String secretKey;

    /** 令牌前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** Header头名称 */
    public static final String HEADER_NAME = "Authorization";

    /**
     * 默认加密算法是HS256
     */
    private final SignatureAlgorithm DEFAULT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    /**
     * 对秘钥进行base64
     *
     * @return base64后的秘钥
     */
    private String generateSecret() {
        return Base64Utils.encodeToString(secretKey.getBytes());
    }

    /**
     * 生成过期时间
     *
     * @return 过期时间
     */
    private Date generateExpiration() {
        return new Date(System.currentTimeMillis() + expire);
    }

    /**
     * 根据claims 和 subject生成token
     *
     * @param claims  claims键值对
     * @param subject 用户名
     * @return token
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        Optional.ofNullable(claims).orElseThrow(() -> new NullPointerException("claims参数不能为null"));
        if (StringUtils.isEmpty(subject)) {
            throw new NullPointerException("subject参数不能为null或者空串");
        }
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(generateExpiration())
                .signWith(DEFAULT_SIGNATURE_ALGORITHM, generateSecret());
        return builder.compact();
    }


    /**
     * 根据UserDetails生成Claims
     *
     * @param userDetails userDetails
     * @throws NullPointerException 当userDetails参数为null时抛出
     * @return 返回生成的Claims键值对，当userDetails为null时返回null
     */
    private Map<String, Object> userDetailsToClaims(UserDetails userDetails) {
        Optional.ofNullable(userDetails).orElseThrow(() -> new NullPointerException("userDetails参数不能为null"));
        Map<String, Object> map = new HashMap<>(8);
        map.put("id", ((ReUser) userDetails).getId());
        map.put("username", userDetails.getUsername());
        map.put("isAccountNonExpired", userDetails.isAccountNonExpired());
        map.put("isAccountNonLocked", userDetails.isAccountNonLocked());
        map.put("isCredentialsNonExpired", userDetails.isCredentialsNonExpired());
        map.put("isEnabled", userDetails.isEnabled());
        return map;
    }


    /**
     * 直接根据UserDetails对象生成token
     *
     * @param userDetails UserDetails对象
     * @throws NullPointerException 当userDetails参数为null时抛出
     * @return 生成的token
     */
    public String generateToken(UserDetails userDetails) {
        Optional.ofNullable(userDetails)
                .orElseThrow(() -> new NullPointerException("userDetails参数不能为null"));
        JwtBuilder builder = Jwts.builder()
                .setClaims(userDetailsToClaims(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(generateExpiration())
                .signWith(DEFAULT_SIGNATURE_ALGORITHM, generateSecret());
        return builder.compact();
    }

    /**
     * 判断token是否过期
     * @param token token
     * @throws NullPointerException 当token为null或空串时抛出
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
        if (StringUtils.isEmpty(token)) {
            throw new NullPointerException("token参数不能为null");
        }
        JwtParser parser = Jwts.parser();
        return parser.setSigningKey(generateSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证token是否合法
     * @param token token
     * @param userDetails userDetails
     * @throws NullPointerException 当token为null或空串、userDetails为null时抛出
     * @return 合法返回true,不合法返回false
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        Optional.ofNullable(userDetails)
                .orElseThrow(() -> new NullPointerException("userDetails参数不能为null"));
        // token校验方式  这里只校验了用户名
        String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
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

    /**
     * 根据token解析出UserDetails对象
     * @param token token
     * @return 此方法返回 {@link ReUser} 对象
     */
    public UserDetails getUserDetailsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        ReUser reUser = new ReUser();
        Integer id = claims.get("id", Integer.class);
        String username = claims.get("username", String.class);
        reUser.setId(id);
        reUser.setUsername(username);
        return reUser;
    }

}
