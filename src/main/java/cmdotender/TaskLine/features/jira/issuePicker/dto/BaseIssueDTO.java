package cmdotender.TaskLine.features.jira.issuePicker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseIssueDTO {
    private String id;
    private String key;
    private String summary;
}
