package cmdotender.TaskLine.features.auth.service;

import cmdotender.TaskLine.features.auth.dto.AtlassianTokenDTO;
import cmdotender.TaskLine.features.auth.dto.AtlassianUserInfoDTO;

public interface AtlassianOauthService {

    AtlassianTokenDTO exchangeCodeForToken(String code);

    AtlassianUserInfoDTO fetchUserInfo(String accessToken);
}
