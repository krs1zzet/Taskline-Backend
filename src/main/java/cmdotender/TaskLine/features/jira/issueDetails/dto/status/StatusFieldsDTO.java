package cmdotender.TaskLine.features.jira.issueDetails.dto.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusFieldsDTO {
    private StatusDTO status;
}
