package cmdotender.TaskLine.features.user.service;

import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDTO save(String username);
    void delete(Long userId);
    UserDTO updateById(Long userId, UserDTO userDTO);
    UserDTO findById(Long userId);
    UserDTO findByUsername(String username);
    Page<UserDTO> findAll(Pageable pageable);
    List<String> getUserRoles(Long userId);
    User findByUsernameReturnUser(String username);
    UserDTO getCurrentUser();
    String getJiraAccessTokenByUserId(Long id);
    void setJiraAccessToken(Long userId, String accessToken);

}
