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

@Service
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.token-validity-in-seconds}")
    private long expirationS;

    private final SecretKey secretKey2 = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    public String createToken(User user) {
        return Jwts.builder()
                   .subject(user.getUserId())
                   .claims()
                        .issuer("boardguide")
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + expirationS * 1000))
                        .and()
                   .signWith(secretKey2,Jwts.SIG.HS512)
                   .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser().decryptWith(secretKey2).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}
