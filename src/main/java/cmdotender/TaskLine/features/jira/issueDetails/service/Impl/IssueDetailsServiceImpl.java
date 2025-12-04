package cmdotender.TaskLine.features.jira.issueDetails.service.Impl;

import cmdotender.TaskLine.features.auth.service.JiraApiClient;
import cmdotender.TaskLine.features.jira.issueDetails.dto.JiraIssueDTO;
import cmdotender.TaskLine.features.jira.issueDetails.dto.ReporterFieldsDTO;
import cmdotender.TaskLine.features.jira.issueDetails.dto.TeamMembersFieldsDTO;
import cmdotender.TaskLine.features.jira.issueDetails.service.IssueDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class IssueDetailsServiceImpl implements IssueDetailsService {

    private final JiraApiClient jiraApiClient;

    @Override
    public JiraIssueDTO<TeamMembersFieldsDTO> getProjectTeamByIssueKey(String issueKey) {

        String path = UriComponentsBuilder
                .fromPath("/issue/{issueKey}")
                .queryParam("fields", "customfield_11274")
                .buildAndExpand(issueKey)
                .encode()
                .toUriString();

        return jiraApiClient.get(
                path,
                new ParameterizedTypeReference<JiraIssueDTO<TeamMembersFieldsDTO>>() {}
        );
    }

    @Override
    public JiraIssueDTO<ReporterFieldsDTO> getIssueReporterByIssueKey(String issueKey) {
        String path = UriComponentsBuilder
                .fromPath("/issue/{issueKey}")
                .queryParam("fields", "reporter")
                .buildAndExpand(issueKey)
                .encode()
                .toUriString();

        return jiraApiClient.get(
                path,
                new ParameterizedTypeReference<JiraIssueDTO<ReporterFieldsDTO>>() {}
        );
    }


    @Override
    public List<JiraIssueDTO<ReporterFieldsDTO>> getRelatedIssueDetailsWithJiraUserId(String jiraUserId) {

        List<String> issueIds = getIssueIdsByJiraUserId(jiraUserId);
        List<JiraIssueDTO<ReporterFieldsDTO>> issues = new ArrayList<>();

        for (String issueId : issueIds) {
            JiraIssueDTO<ReporterFieldsDTO> issue = getIssueReporterByIssueKey(issueId);
            issues.add(issue);
        }
        return issues;

    }

    @Override
    public List<String> getIssueIdsByJiraUserId(String jiraUserId) {
        String jql = "assignee = \"" + jiraUserId + "\""
                + " AND reporter = \"" + jiraUserId + "\""
                + " AND project in (PPM)"
                + " ORDER BY updated DESC";

        String url = UriComponentsBuilder
                .fromPath("/search/jql")
                .queryParam("jql", jql)
                .queryParam("fields", "none")
                .queryParam("maxResults", 100)
                .build()
                .toUriString();

        Map<String, Object> resp = jiraApiClient.get(url, new ParameterizedTypeReference<Map<String, Object>>() {});

        List<Map<String, Object>> issues = (List<Map<String, Object>>) resp.get("issues");
        if (issues == null) return List.of();

        return issues.stream()
                .map(i -> (String) i.get("id"))
                .toList();
    }


}
