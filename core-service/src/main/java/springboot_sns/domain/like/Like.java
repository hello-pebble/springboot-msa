package springboot_sns.domain.like;

import com.pebble.springboot_sns.domain.common.BaseEntity;
import lombok.Getter;

@Entity
@Table(
        name = "likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)
@Getter
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    protected Like() {
    }

    public Like(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
