package cmdotender.TaskLine.features.jira.issueDetails.dto;

import cmdotender.TaskLine.features.jira.jiraUser.DTO.JiraUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamMembersFieldsDTO {

    @JsonProperty("customfield_11274")
    private List<JiraUserDTO> members;


}
