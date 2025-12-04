package cmdotender.TaskLine.features.user.repository;

import cmdotender.TaskLine.features.user.entity.User;
import cmdotender.TaskLine.features.user.entity.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserIdentityRepository extends JpaRepository<UserIdentity, Long> {

    Optional<UserIdentity> findByProviderAndExternalId(String provider, String externalId);

    List<UserIdentity> findByUserId(Long user_id);

    UserIdentity findByUserIdAndProvider(Long userId, String provider);

    @Query("select ui.externalId from UserIdentity ui where ui.user.id = :userId and ui.provider = 'JIRA'")
    Optional<String> findJiraExternalIdByUserId(@Param("userId") Long userId);

}
