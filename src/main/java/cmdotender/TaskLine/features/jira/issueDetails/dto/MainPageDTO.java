package cmdotender.TaskLine.features.jira.issueDetails.dto;

import cmdotender.TaskLine.features.jira.jiraUser.DTO.JiraUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainPageDTO {
    List<JiraIssueDTO<ReporterFieldsDTO>> IssuesWithReporters;
}
