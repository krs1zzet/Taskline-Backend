package cmdotender.TaskLine.features.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JiraContext {
    private String cloudId;
    private String baseApiUrl;
    private String accessToken;
}