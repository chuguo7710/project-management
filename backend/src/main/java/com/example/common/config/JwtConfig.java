package com.example.common.config;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret; // 密匙
    private SecretKey secretKey; // 密匙对象
    private long expirationTime = 1000 * 60 * 60 * 2; // 过期时间

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }
}