package cmdotender.TaskLine.features.jira.issuePicker.dto;

import lombok.Data;

import java.util.List;

@Data
public class IssuePickerResponseDTO {
    private List<IssuePickerSectionDTO> sections;
}