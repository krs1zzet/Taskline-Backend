package cmdotender.TaskLine.features.jira.issueDetails.service;

import cmdotender.TaskLine.features.jira.issueDetails.dto.IssueFieldsDTO;
import cmdotender.TaskLine.features.jira.issueDetails.dto.JiraIssueDTO;
import cmdotender.TaskLine.features.jira.issueDetails.dto.ReporterFieldsDTO;
import cmdotender.TaskLine.features.jira.issueDetails.dto.TeamMembersFieldsDTO;

import java.util.List;

public interface IssueDetailsService {
    JiraIssueDTO<TeamMembersFieldsDTO> getProjectTeamByIssueKey(String issueKey);
    JiraIssueDTO<IssueFieldsDTO> getIssueReporterByIssueKey(String issueKey);
     List<JiraIssueDTO<IssueFieldsDTO>> getRelatedIssueDetailsWithJiraUserId();
    List<String> getIssueIdsByJiraUserId(String jiraUserId);
     List<JiraIssueDTO<IssueFieldsDTO>> getRelatedIssueDetailsWithJiraUserIdExternal(String userId);

    }
