package cmdotender.TaskLine.features.jira.issuePicker.dto;

import lombok.Data;

@Data
public class IssueSuggestionDTO {
    private String id;
    private String key;
    private String summary;
    private String summaryText;
}
