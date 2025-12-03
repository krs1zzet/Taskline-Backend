package cmdotender.TaskLine.features.jira.issuePicker.service;

import cmdotender.TaskLine.features.jira.issuePicker.dto.BaseIssueDTO;

import java.util.List;

public interface IssueService {
    List<BaseIssueDTO> IssuePicker(String query);
}
