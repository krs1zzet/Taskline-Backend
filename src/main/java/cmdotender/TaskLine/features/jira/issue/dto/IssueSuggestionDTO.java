package cmdotender.TaskLine.features.jira.issue.dto;

import lombok.Data;

@Data
public class IssueSuggestionDTO {
    private String id;
    private String key;
    private String summary;
    private String summaryText;
}
