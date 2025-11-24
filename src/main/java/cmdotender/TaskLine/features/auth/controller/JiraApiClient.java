package cmdotender.TaskLine.features.auth.controller;

import cmdotender.TaskLine.features.auth.dto.JiraContext;
import cmdotender.TaskLine.features.auth.service.Impl.JiraContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JiraApiClient {

    private final AtlassianClient atlassianClient;
    private final JiraContextService jiraContextService;

    private String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }
        return path.startsWith("/") ? path : "/" + path;
    }

    public <T> T get(String path, Class<T> responseType) {
        JiraContext ctx = jiraContextService.getContextForCurrentUser();

        String normalizedPath = normalizePath(path);
        String url = ctx.getBaseApiUrl() + normalizedPath;

        log.info("Calling Jira GET: {}", url);

        return atlassianClient.get(url, ctx.getAccessToken(), responseType);
    }

    public <T> T post(String path, Object body, Class<T> responseType) {
        JiraContext ctx = jiraContextService.getContextForCurrentUser();

        String normalizedPath = normalizePath(path);
        String url = ctx.getBaseApiUrl() + normalizedPath;

        log.info("Calling Jira POST: {}", url);

        return atlassianClient.post(url, ctx.getAccessToken(), body, responseType);
    }
}
