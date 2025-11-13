package cmdotender.TaskLine.product.security.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "security.jwt")
@Validated
@Data
public class JwtProperties {

    @NotBlank
    private String secret;

    @Min(1)
    private long accessTtlSeconds = 900;
    private String issuer = "taskline";
    private Cookie cookie = new Cookie();
    private String debug = "false";

    public Duration getAccessTtl() {
        return Duration.ofSeconds(accessTtlSeconds);
    }

    @Data
    public static class Cookie {
        @NotBlank
        private String name = "ACCESS_TOKEN";
        private SameSite sameSite = SameSite.Lax;
        private boolean secure = true;
        private boolean httpOnly = true;
        @NotBlank
        private String path = "/";
        private String domain;
    }
    public enum SameSite {
        None, Lax, Strict
    }
}
