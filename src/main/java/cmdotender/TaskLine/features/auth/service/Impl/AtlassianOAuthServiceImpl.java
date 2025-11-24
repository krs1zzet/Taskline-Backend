package cmdotender.TaskLine.features.auth.service.Impl;

import cmdotender.TaskLine.features.auth.dto.AtlassianTokenDTO;
import cmdotender.TaskLine.features.auth.dto.AtlassianUserInfoDTO;
import cmdotender.TaskLine.features.auth.service.AtlassianOauthService;
import cmdotender.TaskLine.product.security.config.AtlassianOAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class AtlassianOAuthServiceImpl implements AtlassianOauthService {

    private final AtlassianOAuthProperties props;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AtlassianTokenDTO exchangeCodeForToken(String code) {

        String url = "https://auth.atlassian.com/oauth/token";

        Map<String, Object> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("client_id", props.getClientId());
        body.put("client_secret", props.getClientSecret());
        body.put("code", code);
        body.put("redirect_uri", props.getRedirectUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<AtlassianTokenDTO> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, AtlassianTokenDTO.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Failed to exchange code for token. Status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to exchange code for token");
        }

        return response.getBody();
    }



    @Override
    public AtlassianUserInfoDTO fetchUserInfo(String accessToken) {

        String url = "https://api.atlassian.com/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.setAcceptCharset(java.util.List.of(StandardCharsets.UTF_8));
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<AtlassianUserInfoDTO> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, AtlassianUserInfoDTO.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Failed to fetch user info from Atlassian. Status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to fetch user info");
        }

        return response.getBody();
    }
}
