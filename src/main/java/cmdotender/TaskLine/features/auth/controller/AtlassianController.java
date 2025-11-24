package cmdotender.TaskLine.features.auth.controller;

import cmdotender.TaskLine.features.auth.dto.AtlassianTokenDTO;
import cmdotender.TaskLine.features.auth.dto.AtlassianUserInfoDTO;
import cmdotender.TaskLine.features.auth.dto.AuthDTO;
import cmdotender.TaskLine.features.auth.service.AtlassianOauthService;
import cmdotender.TaskLine.features.auth.service.AuthenticationService;
import cmdotender.TaskLine.features.auth.service.JwtService;
import cmdotender.TaskLine.product.security.config.AtlassianOAuthProperties;
import cmdotender.TaskLine.product.security.config.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Controller
@RequestMapping("/api/atlassian/OAuth")
@RequiredArgsConstructor
@Slf4j
public class AtlassianController {

    private final AtlassianOAuthProperties atlassianOAuthProperties;
    private final AtlassianOauthService atlassianOAuthService;
    private final AuthenticationService authenticationService;
    private final JwtProperties jwtProperties;


    @GetMapping("/authorize")
    public void redirectToJira(HttpServletResponse response) throws IOException {

        String clientId = atlassianOAuthProperties.getClientId();
        String redirectUri = atlassianOAuthProperties.getRedirectUri();
        String scopes = atlassianOAuthProperties.getScopes();

        String encodedRedirect = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String encodedScopes = URLEncoder.encode(scopes, StandardCharsets.UTF_8);

        String state = UUID.randomUUID().toString();

        String url = "https://auth.atlassian.com/authorize"
                + "?audience=api.atlassian.com"
                + "&client_id=" + clientId
                + "&scope=" + encodedScopes
                + "&redirect_uri=" + encodedRedirect
                + "&state=" + state
                + "&response_type=code"
                + "&prompt=consent";
        log.info("Jira authorize redirect_uri={}", redirectUri);
        log.info("Jira authorize URL={}", url);

        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public void jiraCallback(
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "error", required = false) String error,
            HttpServletResponse response
    ) throws IOException {

        if (error != null) {
            log.warn("Jira OAuth error: {}", error);
            response.sendRedirect(atlassianOAuthProperties.getFrontendRedirectUri() + "?oauthError=" + URLEncoder.encode(error, StandardCharsets.UTF_8));
            return;
        }

        if (code == null) {
            response.sendRedirect(atlassianOAuthProperties.getFrontendRedirectUri() + "?oauthError=missing_code");
            return;
        }

        log.debug("Jira OAuth callback, code={}, state={}", code, state);

        AtlassianTokenDTO tokenResponse = atlassianOAuthService.exchangeCodeForToken(code);

        AtlassianUserInfoDTO userInfo = atlassianOAuthService.fetchUserInfo(tokenResponse.getAccessToken());
        log.info("Jira OAuth userInfo={}", userInfo);

        AuthDTO auth = authenticationService.loginWithOAuth(
                "JIRA",
                userInfo.getAccountId(),
                userInfo.getEmail() != null ? userInfo.getEmail() : userInfo.getAccountId() + "@atlassian",
                tokenResponse.getAccessToken()
        );

        String cookieName = jwtProperties.getCookie().getName();
        boolean cookieSecure = jwtProperties.getCookie().isSecure();
        long ttlSeconds = jwtProperties.getAccessTtlSeconds();

        ResponseCookie cookie = ResponseCookie.from(cookieName, auth.getAccessToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(ttlSeconds)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        response.sendRedirect(atlassianOAuthProperties.getFrontendRedirectUri());
    }
}
