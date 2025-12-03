package cmdotender.TaskLine.features.user.service.Impl;

import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.entity.UserIdentity;
import cmdotender.TaskLine.features.user.mapper.UserMapper;
import cmdotender.TaskLine.features.user.repository.UserIdentityRepository;
import cmdotender.TaskLine.features.user.repository.UserRepository;
import cmdotender.TaskLine.features.user.service.UserService;
import cmdotender.TaskLine.product.exceptions.customException.ApiException;
import cmdotender.TaskLine.product.exceptions.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserIdentityRepository userIdentityRepository;

    @Override
    public UserDTO save(String username) {

        if(userRepository.existsByUsername(username)) {
            throw new ApiException(
                    ErrorCode.RESOURCE_ALREADY_EXISTS,
                    "User already exists with username: " + username,
                    Map.of(
                            "resource", "User",
                            "field", "username",
                            "value", username
                    )
            );
        }
        User user = new User();
        user.setUsername(username);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void delete(Long userId) {

        if(!userRepository.existsById(userId)) {
            throw new ApiException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "User not found with id: " + userId,
                    Map.of(
                            "resource", "User",
                            "field", "id",
                            "value", userId
                    )
            );
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO updateById(Long userId, UserDTO userDTO) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        userMapper.updateEntityFromDto(userDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public UserDTO findById(Long userId) {

        return userMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with id: " + userId,
                        Map.of(
                                "resource", "User",
                                "field", "id",
                                "value", userId
                        )
                )));
    }

    @Override
    public UserDTO findByUsername(String username) {
        return userMapper.toDTO(userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with username: " + username,
                        Map.of(
                                "resource", "User",
                                "field", "username",
                                "value", username
                        )
                )));
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with id: " + userId
                ));
        return user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName())
                .toList();
    }

    @Override
    public User findByUsernameReturnUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with username: " + username,
                        Map.of(
                                "resource", "User",
                                "field", "username",
                                "value", username
                        )
                ));
    }

    @Override
    public UserDTO getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null || "anonymous".equals(authentication.getName())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found with username: " + authentication.getName()));
        return userMapper.toDTO(user);
    }

    @Override
    public String getJiraAccessTokenByUserId(Long id) {
        String accessToken = userIdentityRepository.findByUserIdAndProvider(id,"JIRA").getAccessToken();

        if(accessToken == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }

        return accessToken;
    }

    @Override
    public void setJiraAccessToken(Long userId, String accessToken) {
        log.info("Setting JIRA access token for user: " + userId + " with token: " + accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        UserIdentity userIdentity = userIdentityRepository.findByUserIdAndProvider(userId, "JIRA");
        if (userIdentity == null) {
            userIdentity = new cmdotender.TaskLine.features.user.entity.UserIdentity();
            userIdentity.setUser(user);
            userIdentity.setProvider("JIRA");
        }
        userIdentity.setAccessToken(accessToken);
        userIdentityRepository.save(userIdentity);
    }

}
