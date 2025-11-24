package cmdotender.TaskLine.features.role.repository;

import cmdotender.TaskLine.features.role.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByCode(String code);
    Boolean existsByCode(String code);
    @Query("""
       select distinct rp.permission
       from RolePermission rp
       join rp.role r
       join r.userRoles ur
       where ur.user.id = :userId
       """)
    List<Permission> findDistinctByUserId(Long userId);

}
