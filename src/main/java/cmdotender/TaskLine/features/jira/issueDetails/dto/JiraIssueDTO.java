package cmdotender.TaskLine.features.jira.issueDetails.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueDTO<T> {
    private String id;
    private String key;
//    private String self;
    private T fields;
}
