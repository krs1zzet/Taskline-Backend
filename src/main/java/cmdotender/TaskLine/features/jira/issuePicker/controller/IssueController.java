package cmdotender.TaskLine.features.jira.issuePicker.controller;

import cmdotender.TaskLine.features.jira.issuePicker.dto.BaseIssueDTO;
import cmdotender.TaskLine.features.jira.issuePicker.service.IssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/jira/issue")
@RequiredArgsConstructor
@Slf4j
public class IssueController {

    private final IssueService issueService;

    @GetMapping("/picker")
    public ResponseEntity<List<BaseIssueDTO>> picker(@RequestParam String query) {

        List<BaseIssueDTO> issues = issueService.IssuePicker(query);
        log.info("Issue picker response: {}", issues);
        return ResponseEntity.ok(issues);

    }
}
