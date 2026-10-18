package com.example;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void generateJwtSecret() {
        System.out.println("JWT Secret Key: " + Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded()));
    }
}
