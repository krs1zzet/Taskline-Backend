package cmdotender.TaskLine.features.jira.linkTypes.service;

import cmdotender.TaskLine.features.jira.linkTypes.dto.JiraIssueLinkTypeDTO;

import java.util.List;


public interface JiraService {

    List<JiraIssueLinkTypeDTO> getIssueLinkTypes();


}


