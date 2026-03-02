package com.pebble.springboot_sns.domain.user;

import com.pebble.springboot_sns.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    protected User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
