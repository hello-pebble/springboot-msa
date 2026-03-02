package springboot_sns.domain.post;

import com.pebble.springboot_sns.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    List<Post> findAllByIdInAndDeletedAtIsNull(List<Long> ids);

    List<Post> findByUserAndDeletedAtIsNullOrderByCreatedAtDesc(User user);

    List<Post> findByUserAndParentIdIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(User user);

    List<Post> findByDeletedAtIsNullOrderByCreatedAtDesc();

    @Modifying
    @Query("UPDATE Post p SET p.replyCount = p.replyCount + 1 WHERE p.id = :postId")
    void incrementReplyCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.replyCount = p.replyCount - 1 WHERE p.id = :postId AND p.replyCount > 0")
    void decrementReplyCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.repostCount = p.repostCount + 1 WHERE p.id = :postId")
    void incrementRepostCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.repostCount = p.repostCount - 1 WHERE p.id = :postId AND p.repostCount > 0")
    void decrementRepostCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void incrementLikeCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId AND p.likeCount > 0")
    void decrementLikeCount(Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + :count WHERE p.id = :postId")
    void incrementViewCount(Long postId, Long count);
}
