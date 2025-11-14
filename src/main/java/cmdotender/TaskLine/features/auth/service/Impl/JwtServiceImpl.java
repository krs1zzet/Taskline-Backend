package cmdotender.TaskLine.features.auth.service.Impl;

import cmdotender.TaskLine.features.auth.service.JwtService;
import cmdotender.TaskLine.product.security.config.JwtProperties;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.security.Keys;


@Slf4j
@RequiredArgsConstructor
@Data
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperties props;

    private Key signingKey;
    private JwtParser jwtParser;

    @PostConstruct
    void init() {
        this.signingKey = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder()
                .requireIssuer(props.getIssuer())
                .setSigningKey(signingKey)
                .build();
    }


    @Override
    public String generateAccessToken(String username, List<String> roles, Map<String, Object> extraClaims) {
        Instant now = Instant.now();

        JwtBuilder b = Jwts.builder()
                .setIssuer(props.getIssuer())
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(props.getAccessTtlSeconds())))
                .signWith(signingKey, SignatureAlgorithm.HS256);
        if (extraClaims != null && !extraClaims.isEmpty()) b.addClaims(extraClaims);
        if (roles != null) b.claim("roles", roles);
        return b.compact();
    }

    @Override
    public String generateAccessToken(String username, List<String> roles) {
        return generateAccessToken(username, roles, Map.of());
    }

    @Override
    public boolean isValid(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            log.warn("JWT token expired: {}", e.getMessage());
            return false;

        }catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;

        }
    }

    @Override
    public String username(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> roles(String token) {
        Object r = jwtParser.parseClaimsJws(token).getBody().get("roles");
        if (r instanceof List<?> rl) {
            return (List<String>) rl;
        } else {
            return List.of();
        }
    }
}
