package br.com.santosschein.complexo.security;

import br.com.santosschein.complexo.application.service.TokenService;
import br.com.santosschein.complexo.infrastructure.config.AppProperties;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenService implements TokenService {

    private final AppProperties appProperties;

    public JwtTokenService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public String generateAccessToken(Long userId, String email) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(appProperties.jwt().expirationSeconds());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey())
                .compact();
    }

    @Override
    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public Long extractUserId(String token) {
        return Long.valueOf(parse(token).getPayload().getSubject());
    }

    @Override
    public String extractEmail(String token) {
        return parse(token).getPayload().get("email", String.class);
    }

    private io.jsonwebtoken.Jws<io.jsonwebtoken.Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token);
    }

    private SecretKey signingKey() {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(appProperties.jwt().secret().getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Algoritmo SHA-256 indisponivel.", exception);
        }
    }
}
