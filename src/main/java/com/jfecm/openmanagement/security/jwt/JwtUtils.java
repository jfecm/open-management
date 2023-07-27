package com.jfecm.openmanagement.security.jwt;

import com.jfecm.openmanagement.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    public String getAccessTokenGenerated(String username) {
        Date expiration = new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration));
        Date issuedAt = new Date(System.currentTimeMillis());
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSignatureKey(), hs256)
                .compact();
    }

    public void isAccessTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error Invalid Token ".concat(e.getMessage()));
            throw new InvalidTokenException("Error Invalid Token");
        }
    }

    public String getUsernameFromToken(String token) {
        return getSingleClaim(token, Claims::getSubject);
    }

    public <T> T getSingleClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = getAllTokenClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Claims getAllTokenClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
