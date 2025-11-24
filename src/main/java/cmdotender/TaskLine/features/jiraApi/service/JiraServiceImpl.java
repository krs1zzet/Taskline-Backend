package cmdotender.TaskLine.features.jiraApi.service;

import cmdotender.TaskLine.features.auth.controller.AtlassianClient;
import cmdotender.TaskLine.features.auth.controller.JiraApiClient;
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

    private final JiraApiClient jiraApiClient;

    @Override
    public List<JiraIssueLinkTypeDTO> getIssueLinkTypes() {
        JiraIssueLinkResponse response =
                jiraApiClient.get("/issueLinkType", JiraIssueLinkResponse.class);

        return response.getIssueLinkTypes();
    }




}
