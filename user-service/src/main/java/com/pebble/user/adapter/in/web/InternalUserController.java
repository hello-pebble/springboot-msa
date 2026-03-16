package com.pebble.user.adapter.in.web;

import com.pebble.user.adapter.in.web.dto.FindOrCreateUserRequest;
import com.pebble.user.adapter.in.web.dto.UserResponseDto;
import com.pebble.user.application.port.in.UserUseCase;
import com.pebble.user.common.WebAdapter;
import com.pebble.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/users")
public class InternalUserController {

    private final UserUseCase userUseCase;

    @PostMapping("/find-or-create")
    public ResponseEntity<UserResponseDto> findOrCreateUser(@Valid @RequestBody FindOrCreateUserRequest request) {
        User user = userUseCase.findOrCreate(
                request.getProvider(),
                request.getProviderId(),
                request.getEmail(),
                request.getDisplayName(),
                request.getProfileImageUrl()
        );
        
        return ResponseEntity.ok(UserResponseDto.from(user));
    }
}
