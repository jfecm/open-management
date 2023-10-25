package com.jfecm.openmanagement.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private long timeExpiration;

    /**
     * Returns the subject (username) of the token.
     * @param token token
     * @return username
     */
    public String getSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Return the secret key
     * @return secret key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Returns all the claims of the token
     * @param token token
     * @return Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    /**
     * Returns a specific token claim
     * @param token token
     * @param claimsResolver claimsResolver
     * @return claim
     * @param <T> Function
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Returns the generated token
     * @param user user details
     * @return token
     */
    public String getToken(UserDetails user) {
        Date issuedAt            = new Date(System.currentTimeMillis());
        Date expiration          = new Date(System.currentTimeMillis() + timeExpiration);
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;

        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSignInKey(), hs256)
                .compact();
    }

    /**
     * Check if the token is valid.
     * Check the user.
     * Check expiration date.
     * @param token token
     * @param userDetails user
     * @return TRUE valid. FALSE. no valid.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getSubject(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
