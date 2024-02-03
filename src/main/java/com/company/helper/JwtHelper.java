package com.company.helper;

import com.company.config.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Sukhrob
 */
@Component
public class JwtHelper {

    @Value("${tokens.access.expiration-time}")
    private int ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${tokens.refresh.expiration-time}")
    private int REFRESH_TOKEN_EXPIRATION_TIME;

    @Value("${tokens.sign-key}")
    private String JWT_SIGN_KEY;

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public String extractType(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("typ", String.class);
    }

    public Boolean validateToken(String token, CustomUserDetails user) {
        final String uid = extractUserId(token);
        return (uid.equals(user.getId()) && !isTokenExpired(token));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        boolean isExpired;
        try {
            isExpired = (extractExpiration(token)).before(new Date());
        } catch (JwtException ex) {
            isExpired = true;
        }
        return isExpired;
    }

    public String generateAccessToken(String userId, String id) {
        return createToken(userId, id, "access", ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String generateRefreshToken(String userId, String id) {
        return createToken(userId, id, "refresh", REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SIGN_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(String userId, String id, String type, int expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("typ", type); // Token type (access or refresh)

        return Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000L))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}