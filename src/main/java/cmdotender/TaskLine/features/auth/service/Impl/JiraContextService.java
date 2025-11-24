package cmdotender.TaskLine.features.auth.service.Impl;


import cmdotender.TaskLine.features.auth.service.AtlassianClient;
import cmdotender.TaskLine.features.auth.dto.AccessibleResourceDTO;
import cmdotender.TaskLine.features.auth.dto.JiraContext;
import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.user.service.UserService;
import cmdotender.TaskLine.product.exceptions.customException.ApiException;
import cmdotender.TaskLine.product.exceptions.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JiraContextService {


    private final AtlassianClient atlassianClient;
    private final UserService userService;

    public JiraContext getContextForCurrentUser() {
        UserDTO userDTO = Optional.ofNullable(userService.getCurrentUser())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));

        String accessToken = Optional.ofNullable(userService.getJiraAccessTokenByUserId(userDTO.getId()))
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Jira accessToken not found"));

        AccessibleResourceDTO[] resources = atlassianClient.get(
                "https://api.atlassian.com/oauth/token/accessible-resources",
                accessToken,
                AccessibleResourceDTO[].class
        );

        if (resources == null || resources.length == 0) {
            throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "No accessible Atlassian resources found");
        }

        AccessibleResourceDTO jiraResource = Arrays.stream(resources)
                .filter(r -> r.getUrl() != null && r.getUrl().contains("sanipak.atlassian.net"))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Jira resource not found"));

        String cloudId = jiraResource.getId();
        String baseApiUrl = String.format(
                "https://api.atlassian.com/ex/jira/%s/rest/api/3",
                cloudId
        );

        log.debug("Resolved Jira context. cloudId={}, baseApiUrl={}", cloudId, baseApiUrl);

        return new JiraContext(cloudId, baseApiUrl, accessToken);
    }
}

