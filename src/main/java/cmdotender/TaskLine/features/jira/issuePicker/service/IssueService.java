package cmdotender.TaskLine.features.jira.issuePicker.service;

import cmdotender.TaskLine.features.jira.issuePicker.dto.BaseIssuePickerDTO;

import java.util.List;

public interface IssueService {
    List<BaseIssuePickerDTO> issuePicker(String query);
}
