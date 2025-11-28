package cmdotender.TaskLine.features.jira.issue.dto;

import lombok.Data;

import java.util.List;

@Data
public class IssuePickerResponseDTO {
    private List<IssuePickerSectionDTO> sections;
}