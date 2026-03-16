package com.pebble.user.adapter.in.web;

import com.pebble.user.adapter.in.web.dto.*;
import com.pebble.user.application.port.in.ProfileUseCase;
import com.pebble.user.application.port.in.UserUseCase;
import com.pebble.user.common.WebAdapter;
import com.pebble.user.domain.Profile;
import com.pebble.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ProfileController {

    private final ProfileUseCase profileUseCase;
    private final UserUseCase userUseCase;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        User user = userUseCase.findById(userId);
        Profile profile = profileUseCase.getByUserId(userId);
        return ResponseEntity.ok(ProfileResponse.from(profile, user));
    }

    @PutMapping("/me/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody @Valid ProfileUpdateRequest request,
            Authentication authentication) {
        User currentUser = userUseCase.findByUsername(authentication.getName());
        Profile profile = profileUseCase.update(
                currentUser.getId(),
                request.displayName(),
                request.bio(),
                request.profileImageId(),
                request.headerImageId()
        );
        return ResponseEntity.ok(ProfileResponse.from(profile, currentUser));
    }

    @PostMapping("/me/profile/image/init")
    public ResponseEntity<MediaInitResponse> initProfileImage(
            @RequestBody @Valid ProfileImageInitRequest request,
            Authentication authentication) {
        User currentUser = userUseCase.findByUsername(authentication.getName());
        MediaInitResponse result = profileUseCase.initProfileImageUpload(
                currentUser.getId(), request.fileSize(), request.fileName());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/me/profile/image/uploaded")
    public ResponseEntity<Void> uploadedProfileImage(
            @RequestBody @Valid ProfileImageUploadedRequest request,
            Authentication authentication) {
        User currentUser = userUseCase.findByUsername(authentication.getName());
        profileUseCase.updateProfileImage(currentUser.getId(), request.mediaId());
        return ResponseEntity.ok().build();
    }
}
