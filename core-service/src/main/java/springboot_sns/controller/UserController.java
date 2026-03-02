package springboot_sns.controller;

import com.pebble.springboot_sns.controller.dto.LoginRequest;
import com.pebble.springboot_sns.controller.dto.UserResponse;
import com.pebble.springboot_sns.controller.dto.UserSignUpRequest;
import com.pebble.springboot_sns.domain.user.UserService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/api/v1/users/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserSignUpRequest request) {
        UserResponse response = UserResponse.from(userService.signUp(request.username(), request.password()));
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

        UserResponse response = UserResponse.from(userService.findByUsername(request.username()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        UserResponse response = UserResponse.from(userService.findByUsername(authentication.getName()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/users")
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> responses = userService.findAll().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
