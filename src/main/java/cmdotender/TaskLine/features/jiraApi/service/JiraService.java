package cmdotender.TaskLine.features.jiraApi.service;

import cmdotender.TaskLine.features.jiraApi.dto.JiraIssueLinkTypeDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface JiraService {

    List<JiraIssueLinkTypeDTO> getIssueLinkTypes();


}


