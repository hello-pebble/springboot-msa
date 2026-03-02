package springboot_sns.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameAndDeletedAtIsNull(String username);

    Optional<User> findByUsernameAndDeletedAtIsNull(String username);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);
}
