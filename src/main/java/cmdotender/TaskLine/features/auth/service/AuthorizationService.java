package cmdotender.TaskLine.features.auth.service;

import java.util.List;

public interface AuthorizationService {

    List<String> getRoleCodesByUserId(Long userId);

    List<String> getPermissionCodesByUserId(Long userId);

    boolean hasPermission(Long userId, String permissionCode);
}
