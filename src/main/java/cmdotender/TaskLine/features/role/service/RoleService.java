package cmdotender.TaskLine.features.role.service;

import cmdotender.TaskLine.features.role.dto.PermissionDTO;
import cmdotender.TaskLine.features.role.dto.RoleDTO;
import cmdotender.TaskLine.features.role.dto.request.CreatePermissionRequest;
import cmdotender.TaskLine.features.role.dto.request.CreateRoleRequest;
import cmdotender.TaskLine.features.role.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RoleService {

    RoleDTO save(CreateRoleRequest request);
    void delete(Long id);
    RoleDTO findById(Long id);
    Page<RoleDTO> findAll(Pageable pageable);

    PermissionDTO savePermission(CreatePermissionRequest request);
    void deletePermission(Long id);
    PermissionDTO findPermissionById(Long id);
    Page<PermissionDTO> findAllPermissions(Pageable pageable);

    void assignPermissionToRole(Long roleId, Long permissionId);
    void removePermissionFromRole(Long roleId, Long permissionId);
    void assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);

    List<PermissionDTO> findPermissionsByRoleId(Long roleId);
    List<PermissionDTO> findPermissionsByUserId(Long userId);
    List<RoleDTO> findRolesByUserId(Long userId);


}
