package com.zzy.cloudbloguser.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zzy
 * @Date 2020/12/12 15:52
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtOperator {

    /**
     * 设置密钥
     * 默认：yaoshixinyuanjieyijiuhaole
     */
    @Value("${secret:yaoshixinyuanjieyijiuhaole}")
    private String secret;

    /**
     * 存活时间
     */
    @Value("${expire-time-in-second:1209600}")
    private Long expirationTimeInSecond;


    /**
     * 从token中获取claims
     * @param token
     * @return
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException exception) {
            log.error("token解析失败：{}", exception);
            throw new IllegalArgumentException("token不合法!");
        }
    }


    /**
     * 获取token的过期时间
     * @param token
     * @return
     */
    private Date getExpirationDateFromToken(String token) {
        return this.getClaimsFromToken(token)
                .getExpiration();

    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        return this.getExpirationDateFromToken(token)
                .before(new Date());
    }

    /**
     * 计算token的过期时间
     * @param token
     * @return
     */
    public Date getExpiredTime(String token) {
        return new Date(System.currentTimeMillis() + this.expirationTimeInSecond * 1000);
    }

    /**
     * 验证token是否合法
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        return !this.isTokenExpired(token);
    }

}
