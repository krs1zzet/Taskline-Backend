package cmdotender.TaskLine.product.security;

import cmdotender.TaskLine.features.role.dto.RoleDTO;
import cmdotender.TaskLine.features.role.service.RoleService;
import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.entity.UserCredential;
import cmdotender.TaskLine.features.user.mapper.UserMapper;
import cmdotender.TaskLine.features.user.repository.UserCredentialRepository;
import cmdotender.TaskLine.features.user.repository.UserRepository;
import cmdotender.TaskLine.features.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cmdotender.TaskLine.features.auth.constants.AuthConstants.CREDENTIAL_TYPE_PASSWORD;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService  implements UserDetailsService {

    private final UserService userService;
    private final UserCredentialRepository userCredentialRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.toEntity(userService.findByUsername(username));
        if(!user.getEnabled()){
            throw new UsernameNotFoundException("User is disabled: " + username);
        }

        String passwordHash = userCredentialRepository
                .findByUserAndTypeAndRevokedFalse(user, CREDENTIAL_TYPE_PASSWORD)
                .map(UserCredential::getSecretHash)
                .orElse("N/A");

        Collection<? extends GrantedAuthority> authorities = buildUserAuthorities(user);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                passwordHash,
                true,
                true,
                true,
                user.getEnabled(),
                authorities
        );


    }

    private Collection<? extends GrantedAuthority> buildUserAuthorities(User user) {
        List<RoleDTO> roles = roleService.findRolesByUserId(user.getId());

        return roles.stream()
                .map(RoleDTO::getCode)
                .map(code -> "ROLE_" + code)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

    }
}
