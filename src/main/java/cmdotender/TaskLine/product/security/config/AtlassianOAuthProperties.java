package cmdotender.TaskLine.product.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "atlassian.oauth")
public class AtlassianOAuthProperties {

    /**
     * Atlassian Developer'da oluşturduğun app'in client id'si
     */
    private String clientId;

    /**
     * Atlassian Developer'da oluşturduğun app'in client secret'ı
     */
    private String clientSecret;

    /**
     * Atlassian'a register ettiğin callback URL
     * Örn: https://localhost:8080/api/auth/oauth/jira/callback
     */
    private String redirectUri;

    /**
     * OAuth scope'ları (boşluk ile ayrılmış)
     * Örn: "read:jira-user read:jira-work offline_access"
     */
    private String scopes;

    /**
     * Login sonrası kullanıcıyı yönlendireceğin frontend URL
     * Örn: http://localhost:3000/app
     */
    private String frontendRedirectUri;
}
