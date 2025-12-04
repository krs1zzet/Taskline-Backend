package cmdotender.TaskLine.features.jira.issueDetails.dto;

import cmdotender.TaskLine.features.jira.jiraUser.DTO.JiraUserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ReporterFieldsDTO {

    @JsonProperty("reporter")
    private JiraUserDTO reporter;
}
