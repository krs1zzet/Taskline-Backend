package cmdotender.TaskLine.features.jiraApi.service;

import cmdotender.TaskLine.features.auth.controller.AtlassianClient;
import cmdotender.TaskLine.features.auth.dto.AccessibleResourceDTO;
import cmdotender.TaskLine.features.jiraApi.dto.JiraIssueLinkResponse;
import cmdotender.TaskLine.features.jiraApi.dto.JiraIssueLinkTypeDTO;
import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.service.UserService;
import cmdotender.TaskLine.product.exceptions.customException.ApiException;
import cmdotender.TaskLine.product.exceptions.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JiraServiceImpl implements JiraService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AtlassianClient atlassianClient;
    private final UserService userService;

    @Override
    public List<JiraIssueLinkTypeDTO> getIssueLinkTypes() {
        UserDTO userDTO = userService.getCurrentUser();
        if (userDTO == null) {
            throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found");
        }

        String accessToken = userService.getJiraAccessTokenByUserId(userDTO.getId());
        if (accessToken == null) {
            throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "AccessToken not found");
        }

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

        String url = String.format(
                "https://api.atlassian.com/ex/jira/%s/rest/api/3/issueLinkType",
                cloudId
        );

        log.info("Fetching Jira issue link types with URL: {}", url);

        JiraIssueLinkResponse response =
                atlassianClient.get(url, accessToken, JiraIssueLinkResponse.class);

        log.info("Jira issue link types fetched: {}", response);

        if (response == null || response.getIssueLinkTypes() == null) {
            log.warn("Jira issue link types response is null or empty");
            return List.of();
        }

        return response.getIssueLinkTypes();

    }



}
