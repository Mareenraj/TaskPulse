package com.mareen.userservice.security.jwt;

import com.mareen.userservice.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${app.jwt.expirationTime}")
    private int jwtTokenExpirationInMs;

    @Value("${app.jwtRefresh.expirationTime}")
    private int refreshTokenExpirationInMs;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date issueDate = new Date();
        Date expiryDate = new Date(issueDate.getTime() + jwtTokenExpirationInMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        claims.put("userId", userDetails.getId());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setClaims(claims)
                .issuedAt(issueDate)
                .expiration(expiryDate)
                .signWith(key())
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        String userName = authentication.getName();
        Date issueDate = new Date();
        Date expiryDate = new Date(issueDate.getTime() + refreshTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(userName)
                .issuedAt(issueDate)
                .expiration(expiryDate)
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(jwtToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        return validateJwtToken(token);
    }
}
