package com.ckdwls.boardguide.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import com.ckdwls.boardguide.Entity.User;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    private String keyBase64Encoded;
    

    @Value("${jwt.token-validity-in-seconds}")
    private long expirationS;

    private SecretKey secretKey;
    
    @PostConstruct
    public void init() {
        // secretKeyPlain이 null이 아닌지 확인
        if (secretKeyPlain == null) {
            throw new IllegalArgumentException("Secret key must not be null");
        }

        keyBase64Encoded = Encoders.BASE64.encode(secretKeyPlain.getBytes());
        secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }


    public String createToken(User user) {
        return Jwts.builder()
                   .subject(user.getUserId())
                   .claims()
                        .issuer("boardguide")
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + expirationS * 1000))
                        .and()
                   .signWith(secretKey,Jwts.SIG.HS512)
                   .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser().decryptWith(secretKey).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}
