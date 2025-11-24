package cmdotender.TaskLine.unitTest;

import cmdotender.TaskLine.features.auth.service.Impl.JwtServiceImpl;
import cmdotender.TaskLine.product.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    @Mock
    private JwtProperties props;

    private JwtServiceImpl jwtService;

    private final String SECRET = "super-secret-key-that-is-long-enough-123";
    private final String ISSUER = "test-issuer";
    private final long ACCESS_TTL_SECONDS = 60L;

    private Key signingKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(props.getSecret()).thenReturn(SECRET);
        when(props.getIssuer()).thenReturn(ISSUER);
        when(props.getAccessTtlSeconds()).thenReturn(ACCESS_TTL_SECONDS);

        jwtService = new JwtServiceImpl(props);
        jwtService.init();
        signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void generateAccessToken_shouldContainSubjectIssuerAndRoles() {
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");
        Map<String, Object> extra = Map.of("custom", "value");

        String token = jwtService.generateAccessToken(username, roles, extra);
        Claims claims = Jwts.parserBuilder()
                .requireIssuer(ISSUER)
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertEquals(ISSUER, claims.getIssuer());
        assertEquals(roles, claims.get("roles"));
        assertEquals("value", claims.get("custom"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void generateAccessToken_overloadWithoutExtraClaims_shouldWork() {
        String username = "simpleUser";
        List<String> roles = List.of("ROLE_USER");

        String token = jwtService.generateAccessToken(username, roles);

        Claims claims = Jwts.parserBuilder()
                .requireIssuer(ISSUER)
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertEquals(roles, claims.get("roles"));
    }

    @Test
    void isValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateAccessToken("validUser", List.of("ROLE_USER"));

        boolean valid = jwtService.isValid(token);

        assertTrue(valid);
    }

    @Test
    void isValid_shouldReturnFalseForExpiredToken() {
        Instant past = Instant.now().minusSeconds(3600);
        String expiredToken = Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject("expiredUser")
                .setIssuedAt(Date.from(past.minusSeconds(60)))
                .setExpiration(Date.from(past))
                .signWith(signingKey)
                .compact();

        boolean valid = jwtService.isValid(expiredToken);

        assertFalse(valid);
    }

    @Test
    void isValid_shouldReturnFalseForInvalidSignature() {
        Key anotherKey = Keys.hmacShaKeyFor(
                "another-secret-key-that-is-long-enough-456".getBytes(StandardCharsets.UTF_8)
        );

        String tokenWithAnotherKey = Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TTL_SECONDS)))
                .signWith(anotherKey)
                .compact();

        boolean valid = jwtService.isValid(tokenWithAnotherKey);

        assertFalse(valid);
    }

    @Test
    void username_shouldReturnSubject() {
        String username = "userForUsernameTest";
        String token = jwtService.generateAccessToken(username, List.of("ROLE_USER"));

        String extracted = jwtService.username(token);

        assertEquals(username, extracted);
    }

    @Test
    void roles_shouldReturnRolesClaim() {
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");
        String token = jwtService.generateAccessToken("user", roles);

        List<String> extractedRoles = jwtService.roles(token);

        assertEquals(roles, extractedRoles);
    }

    @Test
    void roles_shouldReturnEmptyListWhenClaimMissing() {
        Instant now = Instant.now();
        String tokenWithoutRoles = Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject("userNoRoles")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ACCESS_TTL_SECONDS)))
                .signWith(signingKey)
                .compact();

        List<String> extractedRoles = jwtService.roles(tokenWithoutRoles);

        assertNotNull(extractedRoles);
        assertTrue(extractedRoles.isEmpty());
    }
}
