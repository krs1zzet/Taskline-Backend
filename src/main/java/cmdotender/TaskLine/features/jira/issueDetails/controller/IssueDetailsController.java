package cmdotender.TaskLine.features.jira.issueDetails.controller;

import cmdotender.TaskLine.features.jira.issueDetails.dto.IssueFieldsDTO;
import cmdotender.TaskLine.features.jira.issueDetails.dto.JiraIssueDTO;
import cmdotender.TaskLine.features.jira.issueDetails.dto.ReporterFieldsDTO;
import cmdotender.TaskLine.features.jira.issueDetails.dto.TeamMembersFieldsDTO;
import cmdotender.TaskLine.features.jira.issueDetails.service.IssueDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jira/issue-details")
@RequiredArgsConstructor
public class IssueDetailsController {

    private final IssueDetailsService issueDetailsService;

    @GetMapping("/{issueKey}/project-team")
    public ResponseEntity<JiraIssueDTO<TeamMembersFieldsDTO>> getProjectTeam(@PathVariable String issueKey) {
        JiraIssueDTO<TeamMembersFieldsDTO> response =
                issueDetailsService.getProjectTeamByIssueKey(issueKey);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{issueKey}/reporter")
    public ResponseEntity<JiraIssueDTO<IssueFieldsDTO>> getIssueReporter(@PathVariable String issueKey){
        JiraIssueDTO<IssueFieldsDTO> response =
                issueDetailsService.getIssueReporterByIssueKey(issueKey);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{jiraUserId}/related-issues-ids")
    public ResponseEntity<List<String>> getRelatedIssuesByJiraUser(@PathVariable String jiraUserId) {
        List<String> issueIds = issueDetailsService.getIssueIdsByJiraUserId(jiraUserId);
        return ResponseEntity.ok(issueIds);
    }

    @GetMapping("/user/related-issues")
    public ResponseEntity<List<JiraIssueDTO<IssueFieldsDTO>>> getRelatedIssuesDetailsByJiraUser() {
        List<JiraIssueDTO<IssueFieldsDTO>> issues =
                issueDetailsService.getRelatedIssueDetailsWithJiraUserId();
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/user/{jiraUserId}/related-issues")
    public ResponseEntity<List<JiraIssueDTO<IssueFieldsDTO>>> getRelatedIssuesDetailsByJiraUser(@PathVariable String jiraUserId) {
        List<JiraIssueDTO<IssueFieldsDTO>> issues =
                issueDetailsService.getRelatedIssueDetailsWithJiraUserIdExternal(jiraUserId);
        return ResponseEntity.ok(issues);
    }
}