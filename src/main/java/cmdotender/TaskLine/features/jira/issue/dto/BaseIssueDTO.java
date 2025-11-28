package cmdotender.TaskLine.features.jira.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseIssueDTO {
    private String id;     // long yerine String önerilir
    private String key;
    private String summary;
}
