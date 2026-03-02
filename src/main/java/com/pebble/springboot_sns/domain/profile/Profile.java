package com.pebble.springboot_sns.domain.profile;

import com.pebble.springboot_sns.domain.common.BaseEntity;
import com.pebble.springboot_sns.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "profiles")
@Getter
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 50)
    private String displayName;

    @Column(length = 160)
    private String bio;

    private Long profileImageId;

    private Long headerImageId;

    protected Profile() {
    }

    public Profile(User user) {
        this.user = user;
    }

    public void update(String displayName, String bio, Long profileImageId, Long headerImageId) {
        this.displayName = displayName;
        this.bio = bio;
        this.profileImageId = profileImageId;
        this.headerImageId = headerImageId;
    }

    public void updateProfileImage(Long profileImageId) {
        this.profileImageId = profileImageId;
    }
}
