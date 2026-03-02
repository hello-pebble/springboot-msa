package springboot_sns.domain.reply;

import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Post, Long> {

    List<Post> findByParentIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long parentId);

    List<Post> findByUserAndParentIdIsNotNullAndDeletedAtIsNullOrderByCreatedAtDesc(User user);
}
