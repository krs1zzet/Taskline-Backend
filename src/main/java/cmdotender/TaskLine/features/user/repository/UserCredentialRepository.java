package cmdotender.TaskLine.features.user.repository;

import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    Optional<UserCredential> findByUserAndTypeAndRevokedFalse(User user, String type);

    List<UserCredential> findByUser(User user);
}
