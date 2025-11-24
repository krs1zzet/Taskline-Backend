package cmdotender.TaskLine.features.role.repository;

import cmdotender.TaskLine.features.role.entity.Permission;
import cmdotender.TaskLine.features.role.entity.Role;
import cmdotender.TaskLine.features.role.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRoleAndPermission(Role role, Permission permission);
    List<RolePermission> findByRole(Role role);

    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    List<RolePermission> findByRoleId(Long roleId);
}
