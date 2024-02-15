package com.demo.foodbasket.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.demo.foodbasket.models.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private UserService userService;

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey())
                .compact();
    }

    public String getUsername(String token) {
        Claims claims = getAllClaims(token);
        return claims.getSubject();
    }

    public Date getExpiration(String token) {
        Claims claims = getAllClaims(token);
        return claims.getExpiration();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    private Boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        final String username = getUsername(token);
        UserDto userDto = userService.getUserByUsername(username);
        
        return (username.equals(userDto.getUsername()) && !isTokenExpired(token));
    }
}
