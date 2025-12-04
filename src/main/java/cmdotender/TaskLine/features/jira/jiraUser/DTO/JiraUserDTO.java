package cmdotender.TaskLine.features.jira.jiraUser.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraUserDTO {
//    private String self;
//    private String accountId;
    private String emailAddress;
    private String displayName;
}
