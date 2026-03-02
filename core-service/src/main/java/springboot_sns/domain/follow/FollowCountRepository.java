package springboot_sns.domain.follow;

import com.pebble.springboot_sns.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowCountRepository extends JpaRepository<FollowCount, Long> {

    Optional<FollowCount> findByUserAndDeletedAtIsNull(User user);

    @Modifying
    @Query("UPDATE FollowCount fc SET fc.followerCount = fc.followerCount + 1 WHERE fc.user.id = :userId AND fc.deletedAt IS NULL")
    void incrementFollowerCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE FollowCount fc SET fc.followerCount = fc.followerCount - 1 WHERE fc.user.id = :userId AND fc.deletedAt IS NULL AND fc.followerCount > 0")
    void decrementFollowerCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE FollowCount fc SET fc.followingCount = fc.followingCount + 1 WHERE fc.user.id = :userId AND fc.deletedAt IS NULL")
    void incrementFollowingCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE FollowCount fc SET fc.followingCount = fc.followingCount - 1 WHERE fc.user.id = :userId AND fc.deletedAt IS NULL AND fc.followingCount > 0")
    void decrementFollowingCount(@Param("userId") Long userId);
}
