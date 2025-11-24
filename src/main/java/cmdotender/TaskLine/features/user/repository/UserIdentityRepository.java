package cmdotender.TaskLine.features.user.repository;

import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.entity.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserIdentityRepository extends JpaRepository<UserIdentity, Long> {

    Optional<UserIdentity> findByProviderAndExternalId(String provider, String externalId);

    List<UserIdentity> findByUserId(Long user_id);

    UserIdentity findByUserIdAndProvider(Long userId, String provider);
}
