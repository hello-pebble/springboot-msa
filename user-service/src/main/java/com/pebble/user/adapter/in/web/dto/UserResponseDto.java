package com.pebble.user.adapter.in.web.dto;

import com.pebble.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String provider;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getProvider());
    }
}
