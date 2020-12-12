package com.zzy.cloudblogblog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * @author zzy
 * @Date 2020/12/12 15:10
 */
@Slf4j
@AllArgsConstructor
@Component
public class JwtOperator {

    /**
     * 密钥
     * 默认：yaoshichangzeyameijiuhaole
     */
    @Value("${secret:yaoshixinyuanjieyijiuhaole}")
    private String secret;

    /**
     * token有效期
     */
    @Value("@{expire-time-in-second:1209600}")
    private Long expirationTimeInSecond;

    /**
     * 从token中获取claim
     * @param token
     * @return
     */
    public Claims getClaimsFromToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(this.secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException exception) {
            log.error("token解析错误:{}",exception);
            throw new IllegalArgumentException("token不合法!");
        }
    }

    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpirationTimeFromToken(String token){
        return getClaimsFromToken(token)
                .getExpiration();
    }

    /**
     * 判断token是否过期 过期返回true，意味着newDate时间在后
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        return getExpirationTimeFromToken(token)
                .before(new Date());
    }


    /**
     * @param
     * @return  token过期时间
     */
    public Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + this.expirationTimeInSecond * 1000);
    }

    public String generateToken(Map<String,Object> claims){
        Date createTime = new Date();
        Date expirationTime = this.getExpirationTime();
        byte[] keyBytes = secret.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createTime)
                .setExpiration(expirationTime)
                .signWith(key)
                .compact();

    }

    /**
     * 判断token是否合法
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        return !this.isTokenExpired(token);
    }


}
