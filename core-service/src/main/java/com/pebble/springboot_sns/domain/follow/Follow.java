package com.pebble.springboot_sns.domain.follow;

import com.pebble.springboot_sns.domain.common.BaseEntity;
import com.pebble.springboot_sns.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        name = "follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
@Getter
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    protected Follow() {
    }

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}
