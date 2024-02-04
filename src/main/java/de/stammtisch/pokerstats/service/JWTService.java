package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private final String secret;
    private final long expiration;

    public JWTService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public @NonNull <T> T extractClaim(String token, @NonNull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, @NonNull User user) {
        if (extraClaims == null) {
            extraClaims = Map.of();
        }
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private @NonNull String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUsernameFromToken(String token) {
        return this.extractUsername(token);
    }

    private @NonNull Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
