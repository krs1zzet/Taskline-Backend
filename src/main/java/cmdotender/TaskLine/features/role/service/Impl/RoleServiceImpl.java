package cmdotender.TaskLine.features.role.service.Impl;

import cmdotender.TaskLine.features.role.dto.PermissionDTO;
import cmdotender.TaskLine.features.role.dto.RoleDTO;
import cmdotender.TaskLine.features.role.dto.request.CreatePermissionRequest;
import cmdotender.TaskLine.features.role.dto.request.CreateRoleRequest;
import cmdotender.TaskLine.features.role.entity.Permission;
import cmdotender.TaskLine.features.role.entity.Role;
import cmdotender.TaskLine.features.role.entity.RolePermission;
import cmdotender.TaskLine.features.role.entity.UserRole;
import cmdotender.TaskLine.features.role.mapper.PermissionMapper;
import cmdotender.TaskLine.features.role.mapper.RoleMapper;
import cmdotender.TaskLine.features.role.repository.PermissionRepository;
import cmdotender.TaskLine.features.role.repository.RolePermissionRepository;
import cmdotender.TaskLine.features.role.repository.RoleRepository;
import cmdotender.TaskLine.features.role.repository.UserRoleRepository;
import cmdotender.TaskLine.features.role.service.RoleService;
import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.repository.UserRepository;
import cmdotender.TaskLine.product.exceptions.customException.ApiException;
import cmdotender.TaskLine.product.exceptions.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public RoleDTO save(CreateRoleRequest request) {
        if(roleRepository.existsByCode(request.getCode())) {
            throw new ApiException(
                    ErrorCode.RESOURCE_ALREADY_EXISTS,
                    "Role already exists with code: " + request.getCode(),
                    Map.of(
                            "resource", "Role",
                            "field", "code",
                            "value", request.getCode()
            )
            );
        }
        Role role = Role.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        log.info("Saving role: {}", role);
        return roleMapper.toDTO(roleRepository.save(role));
    }


    @Override
    public void delete(Long id) {
        if(!roleRepository.existsById(id)) {
            throw new ApiException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Role not found with id: " + id,
                    Map.of(
                            "resource", "Role",
                            "field", "id",
                            "value", id
                    )
            );
        }
        log.info("Delete role with id: {}", id);
        roleRepository.deleteById(id);

    }

    @Override
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() ->
                new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Role not found with id: " + id,
                        Map.of(
                                "resource", "Role",
                                "field", "id",
                                "value", id
                        )
                )
        );
        log.info("Role found with id: {}", id);
        return roleMapper.toDTO(role);
    }

    @Override
    public Page<RoleDTO> findAll(Pageable pageable) {
        log.info("Find all roles");
        return roleRepository.findAll(pageable).map(roleMapper::toDTO);
    }

    @Override
    public PermissionDTO savePermission(CreatePermissionRequest request) {

        if(permissionRepository.existsByCode(request.getCode())) {
            throw new ApiException(
                    ErrorCode.RESOURCE_ALREADY_EXISTS,
                    "Permission already exists with code: " + request.getCode(),
                    Map.of(
                            "resource", "Permission",
                            "field", "code",
                            "value", request.getCode()
                    )
            );
        }
        Permission permission = Permission.builder()
                .code(request.getCode())
                .description(request.getDescription())
                .build();
        log.info("Saving permission: {}", permission);
        return permissionMapper.toDTO(permissionRepository.save(permission));

    }

    @Override
    public void deletePermission(Long id) {
        if(!permissionRepository.existsById(id)) {
            throw new ApiException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Permission not found with id: " + id,
                    Map.of(
                            "resource", "Permission",
                            "field", "id",
                            "value", id
                    )
            );
        }
        log.info("Delete permission with id: {}", id);
        permissionRepository.deleteById(id);

    }

    @Override
    public PermissionDTO findPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(
                () -> new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Permission not found with id: " + id,
                        Map.of(
                                "resource", "Permission",
                                "field", "id",
                                "value", id
                        )
                )
        );
        log.info("Found permission with id {}", id);
        return permissionMapper.toDTO(permission);

    }

    @Override
    public Page<PermissionDTO> findAllPermissions(Pageable pageable) {
        log.info("Find all permissions");
        return permissionRepository.findAll(pageable).map(permissionMapper::toDTO);
    }

    @Override
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() ->
                new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Role not found with id: " + roleId,
                        Map.of(
                                "resource", "Role",
                                "field", "id",
                                "value", roleId
                        )
                )
        );

        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() ->
                new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Permission not found with id: " + permissionId,
                        Map.of(
                                "resource", "Permission",
                                "field", "id",
                                "value", permissionId
                        )
                )
        );

        RolePermission rolePermission = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();

        rolePermissionRepository.save(rolePermission);
        log.info("Assigned permission {} to role {}", permission.getCode(), role.getCode());
    }

    @Override
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        RolePermission rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)
                .orElseThrow(() ->
                        new ApiException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "RolePermission not found with roleId: " + roleId + " and permissionId: " + permissionId,
                                Map.of(
                                        "resource", "RolePermission",
                                        "fields", "roleId, permissionId",
                                        "values", roleId + ", " + permissionId
                                )
                        )
                );
        rolePermissionRepository.delete(rolePermission);
        log.info("Removed permission {} from role {}", permissionId, roleId);
    }

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() ->
                new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Role not found with id: " + roleId,
                        Map.of(
                                "resource", "Role",
                                "field", "id",
                                "value", roleId
                        )
                )
        );

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with id: " + userId,
                        Map.of(
                                "resource", "User",
                                "field", "id",
                                "value", userId
                        )
                )
        );

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();

        userRoleRepository.save(userRole);
        log.info("Assigned role {} to user {}", role.getCode(), user.getUsername());

    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() ->
                new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Role not found with id: " + roleId,
                        Map.of(
                                "resource", "Role",
                                "field", "id",
                                "value", roleId
                        )
                )
        );
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with id: " + userId,
                        Map.of(
                                "resource", "User",
                                "field", "id",
                                "value", userId
                        )
                )
        );
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() ->
                        new ApiException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "UserRole not found with userId: " + userId + " and roleId: " + roleId,
                                Map.of(
                                        "resource", "UserRole",
                                        "fields", "userId, roleId",
                                        "values", userId + ", " + roleId
                                )
                        )
                );
        userRoleRepository.delete(userRole);
        log.info("Removed role {} from user {}", role.getCode(), user.getUsername());

    }

    @Override
    public List<PermissionDTO> findPermissionsByRoleId(Long roleId) {
        if(!roleRepository.existsById(roleId)) {
            throw new ApiException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Role not found with id: " + roleId,
                    Map.of(
                            "resource", "Role",
                            "field", "id",
                            "value", roleId
                    )
            );
        }
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        log.info("Found {} permissions for roleId {}", rolePermissions.size(), roleId);
        List<PermissionDTO> permissions = new ArrayList<>();
        for (RolePermission rp : rolePermissions) {
            permissions.add(permissionMapper.toDTO(rp.getPermission()));
        }
        return permissions;
    }


    @Override
    public List<PermissionDTO> findPermissionsByUserId(Long userId) {
        if(!roleRepository.existsById(userId)) {
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
        List<Permission> permissions = permissionRepository.findDistinctByUserId(userId);
        log.info("Found {} permissions for user {}", permissions.size(), userId);
        return permissions.stream()
                .map(permissionMapper::toDTO)
                .toList();
    }


    @Override
    public List<RoleDTO> findRolesByUserId(Long userId) {
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

        return userRoleRepository.findByUserId(userId).stream()
                .map(userRole -> roleMapper.toDTO(userRole.getRole()))
                .toList();
    }
}
