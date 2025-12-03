package cmdotender.TaskLine.features.jira.linkTypes.controller;

import cmdotender.TaskLine.features.jira.linkTypes.dto.JiraIssueLinkTypeDTO;
import cmdotender.TaskLine.features.jira.linkTypes.service.JiraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/jira/issues")
@RestController
@Slf4j

public class JiraController {

    private final JiraService jiraService;

    @GetMapping
    public ResponseEntity<List<JiraIssueLinkTypeDTO>> getIssueLinkTypes() {
        log.info("Fetching Jira issue link types");
        List<JiraIssueLinkTypeDTO> linkTypes = jiraService.getIssueLinkTypes();
        if(linkTypes == null || linkTypes.isEmpty()) {
            log.warn("No Jira issue link types found");
            log.info("Link type size");
            linkTypes = List.of();
        } else {
            log.info("Successfully fetched {} Jira issue link types", linkTypes.size());
        }
        return ResponseEntity.ok(linkTypes);
    }

}
