package cmdotender.TaskLine.features.jiraApi.service;

import cmdotender.TaskLine.features.auth.service.JiraApiClient;
import cmdotender.TaskLine.features.jiraApi.dto.JiraIssueLinkResponse;
import cmdotender.TaskLine.features.jiraApi.dto.JiraIssueLinkTypeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
