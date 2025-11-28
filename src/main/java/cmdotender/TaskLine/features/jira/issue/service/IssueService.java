package cmdotender.TaskLine.features.jira.issue.service;

import cmdotender.TaskLine.features.jira.issue.dto.BaseIssueDTO;

import java.util.List;

public interface IssueService {
    List<BaseIssueDTO> IssuePicker(String query);
}
