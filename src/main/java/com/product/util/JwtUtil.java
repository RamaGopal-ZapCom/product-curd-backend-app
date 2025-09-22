package com.product.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Component
public class JwtUtil {

    // TODO keep it in application.properties ideally
    private final String SECRET_KEY = "mySecretKey12345mySecretKey12345mySecretKey12345mySecretKey12345";

    // TODO based our requirements we will change 1 hour
    private final long EXPIRATION = 1000 * 60 * 60;

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    public String extractRole(String token) {
        return (String) Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().get("role");
    }

    public String refreshToken(String token) {
        // Extract username and claims from the old token
        String username = extractUsername(token);
        String role = extractRole(token);

        // Generate a new token with fresh expiration
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}
