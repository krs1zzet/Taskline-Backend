package cmdotender.TaskLine.features.jira.issue.dto;

import lombok.Data;

import java.util.List;

@Data
public class IssuePickerSectionDTO {
    private String id;
    private String label;
    private String msg;
    private List<IssueSuggestionDTO> issues;
}