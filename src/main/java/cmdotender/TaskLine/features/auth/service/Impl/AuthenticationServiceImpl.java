package cmdotender.TaskLine.features.auth.service.Impl;

import cmdotender.TaskLine.features.auth.dto.AuthDTO;
import cmdotender.TaskLine.features.role.dto.RoleDTO;
import cmdotender.TaskLine.features.role.entity.Role;
import cmdotender.TaskLine.features.role.mapper.RoleMapper;
import cmdotender.TaskLine.features.role.service.RoleService;
import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.auth.dto.request.ChangePasswordRequest;
import cmdotender.TaskLine.features.auth.dto.request.SignInRequest;
import cmdotender.TaskLine.features.auth.dto.request.SignUpRequest;
import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.entity.UserCredential;
import cmdotender.TaskLine.features.user.entity.UserIdentity;
import cmdotender.TaskLine.features.user.mapper.UserMapper;
import cmdotender.TaskLine.features.user.repository.UserCredentialRepository;
import cmdotender.TaskLine.features.auth.service.AuthenticationService;
import cmdotender.TaskLine.features.auth.service.JwtService;
import cmdotender.TaskLine.features.user.repository.UserIdentityRepository;
import cmdotender.TaskLine.features.user.repository.UserRepository;
import cmdotender.TaskLine.features.user.service.Impl.UserServiceImpl;
import cmdotender.TaskLine.features.user.service.UserService;
import cmdotender.TaskLine.product.exceptions.customException.ApiException;
import cmdotender.TaskLine.product.exceptions.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cmdotender.TaskLine.features.auth.constants.AuthConstants.CREDENTIAL_TYPE_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtService jwtService;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final UserIdentityRepository userIdentityRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;


    @Override
    public UserDTO signUp(SignUpRequest request) {

        UserDTO userDTO = userService.save(request.getUsername());

        User user = userMapper.toEntity(userDTO);

        UserCredential userCredential = UserCredential.builder()
                .user(user)
                .type(CREDENTIAL_TYPE_PASSWORD)
                .secretHash(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .revoked(false)
                .build();
        userCredentialRepository.save(userCredential);

        return userDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthDTO signIn(SignInRequest request) {
        User user = userService.findByUsernameReturnUser(request.getUsername());

        UserCredential credential = userCredentialRepository
                .findByUserAndTypeAndRevokedFalse(user,CREDENTIAL_TYPE_PASSWORD)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.AUTH_INVALID_CREDENTIALS,
                "Invalid username or password"
                ));
        if(!passwordEncoder.matches(request.getPassword(),credential.getSecretHash())) {
            throw new ApiException(
                    ErrorCode.AUTH_INVALID_CREDENTIALS,
                    "Invalid username or password"
            );
        }

        if(!user.getEnabled()){
            throw new ApiException(
                    ErrorCode.AUTH_USER_DISABLED,
                    "User is disabled: " + request.getUsername()
            );
        }

        String accessToken = jwtService.generateAccessToken(
                user.getUsername(),
                getUserRoles(user)
        );

        UserDTO userDTO = userMapper.toDTO(user);

        return AuthDTO.builder()
                .user(userDTO)
                .accessToken(accessToken)
                .build();
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {

    }

    @Override
    public AuthDTO loginWithOAuth(String provider, String externalId, String email, String accessJiraToken) {

        UserIdentity identity = userIdentityRepository
                .findByProviderAndExternalId(provider, externalId)
                .orElse(null);
        if(accessJiraToken == null) {
            if(identity == null) {
                throw new ApiException(
                        ErrorCode.AUTH_INVALID_CREDENTIALS,
                        "No existing identity found for provider: " + provider + " and externalId: " + externalId
                );
            }
            else{
                accessJiraToken = identity.getAccessToken();
            }
        }

        User user;
        if(identity != null) {
            user = identity.getUser();
            userServiceImpl.setJiraAccessToken(user.getId(), accessJiraToken);
        }
        else{
            user = User.builder()
                    .username(email != null ? email : (provider + "_" + externalId))
                    .enabled(true)
                    .build();
            userRepository.save(user);


            UserIdentity newIdentity = UserIdentity.builder()
                    .user(user)
                    .provider(provider)
                    .externalId(externalId)
                    .accessToken(accessJiraToken)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userIdentityRepository.save(newIdentity);
        }

        if(!user.getEnabled()){
            throw new ApiException(
                    ErrorCode.AUTH_USER_DISABLED,
                    "User is disabled: " + user.getUsername()
            );

        }
        String accessToken = jwtService.generateAccessToken(user.getUsername(),getUserRoles(user));
        UserDTO userDTO = userMapper.toDTO(user);
        return AuthDTO.builder()
                .user(userDTO)
                .accessToken(accessToken)
                .build();
    }

    private List<String> getUserRoles(User user) {
        return roleService.findRolesByUserId(user.getId()).stream()
                .map(RoleDTO::getCode)
                .collect(Collectors.toList());
    }



}
