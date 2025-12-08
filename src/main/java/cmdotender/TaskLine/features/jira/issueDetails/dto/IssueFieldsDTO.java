package cmdotender.TaskLine.features.jira.issueDetails.dto;

import cmdotender.TaskLine.features.jira.issueDetails.dto.status.StatusDTO;
import cmdotender.TaskLine.features.jira.jiraUser.DTO.JiraUserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueFieldsDTO {

    private JiraUserDTO reporter;
    private String summary;
    private StatusDTO status;
    @JsonProperty("updated")
    private String updated;
}
