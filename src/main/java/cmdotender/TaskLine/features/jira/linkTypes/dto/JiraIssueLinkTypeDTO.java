package cmdotender.TaskLine.features.jira.linkTypes.dto;

import lombok.Data;

@Data
public class JiraIssueLinkTypeDTO {
    private String id;
    private String name;
    private String inward;
    private String outward;
    private String self;
}
