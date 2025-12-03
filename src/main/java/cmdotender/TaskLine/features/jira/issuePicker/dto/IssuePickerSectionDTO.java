package cmdotender.TaskLine.features.jira.issuePicker.dto;

import lombok.Data;

import java.util.List;

@Data
public class IssuePickerSectionDTO {
    private String id;
    private String label;
    private String msg;
    private List<IssueSuggestionDTO> issues;
}