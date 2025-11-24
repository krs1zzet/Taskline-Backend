package cmdotender.TaskLine.features.role.repository;

import cmdotender.TaskLine.features.role.entity.Role;
import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.role.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    boolean existsByUserAndRole(User user, Role role);

    List<UserRole> findByUser(User user);

    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    List<UserRole> findByUserId(Long userId);
}
