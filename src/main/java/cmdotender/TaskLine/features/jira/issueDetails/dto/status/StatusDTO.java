package cmdotender.TaskLine.features.jira.issueDetails.dto.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusDTO {
    private String id;
    private String name;

    @JsonProperty("statusCategory")
    private StatusCategoryDTO statusCategory;
}
