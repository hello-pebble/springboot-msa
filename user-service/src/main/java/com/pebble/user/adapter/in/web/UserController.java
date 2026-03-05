package com.pebble.user.adapter.in.web;

import com.pebble.user.adapter.in.web.dto.LoginRequest;
import com.pebble.user.adapter.in.web.dto.UserResponse;
import com.pebble.user.adapter.in.web.dto.UserSignUpRequest;
import com.pebble.user.application.port.in.UserUseCase;
import com.pebble.user.common.WebAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/api/v1/users/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserSignUpRequest request) {
        UserResponse response = UserResponse.from(userUseCase.signUp(request.username(), request.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<UserResponse> login(@ModelAttribute LoginRequest request,
                                                HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, httpRequest, httpResponse);

        UserResponse response = UserResponse.from(userUseCase.findByUsername(request.username()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        UserResponse response = UserResponse.from(userUseCase.findByUsername(authentication.getName()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/users")
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> responses = userUseCase.findAll().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
