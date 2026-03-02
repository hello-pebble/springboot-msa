package springboot_sns.domain.follow;

import com.pebble.springboot_sns.domain.common.BaseEntity;
import com.pebble.springboot_sns.domain.user.User;
import lombok.Getter;

@Entity
@Table(name = "follow_counts")
@Getter
public class FollowCount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private Long followerCount = 0L;

    @Column(nullable = false)
    private Long followingCount = 0L;

    protected FollowCount() {
    }

    public FollowCount(User user) {
        this.user = user;
        this.followerCount = 0L;
        this.followingCount = 0L;
    }
}
