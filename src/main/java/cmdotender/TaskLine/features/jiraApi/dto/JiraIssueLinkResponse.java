package cmdotender.TaskLine.features.jiraApi.dto;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class JiraIssueLinkResponse {

    @JsonProperty("issueLinkTypes") // JSON key bu
    private List<JiraIssueLinkTypeDTO> issueLinkTypes;

}

