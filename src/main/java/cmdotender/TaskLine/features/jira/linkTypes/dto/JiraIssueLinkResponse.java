package cmdotender.TaskLine.features.jira.linkTypes.dto;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class JiraIssueLinkResponse {

    @JsonProperty("issueLinkTypes") // JSON key bu
    private List<JiraIssueLinkTypeDTO> issueLinkTypes;

}

