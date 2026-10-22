package com.example.common.utils;

import com.example.common.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTools {

    @Autowired
    private JwtConfig jwtConfig;
    private SecretKey secretKey;
    private long expirationTime;

    @PostConstruct
    public void init() {
        secretKey = jwtConfig.getSecretKey();
        expirationTime = jwtConfig.getExpirationTime();
    }

    /**
     * 生成 JWT 令牌
     * @param userId   用户唯一标识
     * @return 生成的令牌字符串
     */
    //生成令牌
    public String generateToken(Long userId) {
        // 自定义数据
        // Map<String, Object> claims = new HashMap<>();

        // 获取当前时间和过期时间
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);
        // 构建并生成JWT
        return Jwts.builder()                       // 创建一个JWT构建器
                .subject(userId.toString())         // 设置唯一标识(非必须)
                .issuedAt(now)                      // 设置令牌的签发时间
                .expiration(expiration)             // 设置令牌的过期时间
                .signWith(secretKey)                // 设置密钥
                .compact();                         // 生成最终的令牌jjwt-api
    }

    /**
     * 校验并解析 JWT 令牌
     * @param token 客户端传来的令牌字符串
     * @return 解析出的载荷数据（Claims）
     * @throws ExpiredJwtException 令牌已过期
     * @throws SignatureException 签名验证失败（令牌被篡改）
     * */
    //解析令牌
    public Claims parseToken(String token) {
            return Jwts.parser()                    // 创建一个JWT解析器
                    .verifyWith(secretKey)          // 设置密钥
                    .build()                        // 构建一个线程安全的解析器实例
                    .parseSignedClaims(token)       // 解析,验证密匙和过期时间
                    .getPayload();                  // 从解析结果获取自定义对象
    }

}
