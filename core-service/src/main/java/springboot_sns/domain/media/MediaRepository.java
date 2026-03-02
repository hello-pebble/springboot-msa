package springboot_sns.domain.media;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findByIdAndDeletedAtIsNull(Long id);
}
