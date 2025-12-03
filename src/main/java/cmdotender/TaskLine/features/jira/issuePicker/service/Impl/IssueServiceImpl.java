package cmdotender.TaskLine.features.jira.issuePicker.service.Impl;

import cmdotender.TaskLine.features.auth.service.JiraApiClient;
import cmdotender.TaskLine.features.jira.issuePicker.dto.BaseIssueDTO;
import cmdotender.TaskLine.features.jira.issuePicker.dto.IssuePickerResponseDTO;
import cmdotender.TaskLine.features.jira.issuePicker.service.IssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueServiceImpl implements IssueService {

    private final JiraApiClient jiraApiClient;

    @Override
    public List<BaseIssueDTO> IssuePicker(String query) {

        String path = UriComponentsBuilder
                .fromPath("/issue/picker")
                .queryParam("query", query)
                .build()
                .encode()
                .toUriString();

        IssuePickerResponseDTO response = jiraApiClient.get(path, IssuePickerResponseDTO.class);

        if (response.getSections() == null) return List.of();

        return response.getSections().stream()
                .filter(s -> s.getIssues() != null)
                .flatMap(s -> s.getIssues().stream())
                .map(i -> new BaseIssueDTO(
                        i.getId(),
                        i.getKey(),
                        (i.getSummaryText() != null && !i.getSummaryText().isBlank())
                                ? i.getSummaryText()
                                : i.getSummary()
                ))
                .toList();
    }


}
