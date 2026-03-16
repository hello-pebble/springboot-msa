package com.pebble.auth.client;

import com.pebble.auth.client.dto.FindOrCreateUserRequest;
import com.pebble.auth.client.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @PostMapping("/api/v1/internal/users/find-or-create")
    ResponseEntity<UserResponseDto> findOrCreateUser(@RequestBody FindOrCreateUserRequest request);

}
