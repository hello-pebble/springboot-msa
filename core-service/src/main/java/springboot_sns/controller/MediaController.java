package springboot_sns.controller;

import com.pebble.springboot_sns.controller.dto.MediaInitRequest;
import com.pebble.springboot_sns.controller.dto.MediaInitResponse;
import com.pebble.springboot_sns.controller.dto.MediaPresignedUrlResponse;
import com.pebble.springboot_sns.controller.dto.MediaUploadedRequest;
import com.pebble.springboot_sns.domain.media.MediaService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final UserService userService;

    @PostMapping("/api/v1/media/init")
    public ResponseEntity<MediaInitResponse> init(@Valid @RequestBody MediaInitRequest request,
                                                   Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        MediaService.UploadInitResult result = mediaService.initUpload(
                currentUser.getId(), request.mediaType(), request.fileSize(), request.fileName());
        return ResponseEntity.status(HttpStatus.CREATED).body(MediaInitResponse.from(result));
    }

    @GetMapping("/api/v1/media/{id}/presigned-url")
    public ResponseEntity<MediaPresignedUrlResponse> getPresignedUrl(@PathVariable Long id) {
        MediaService.MediaUrlResult result = mediaService.getPresignedUrl(id);
        return ResponseEntity.ok(new MediaPresignedUrlResponse(result.presignedUrl(), result.mediaType()));
    }

    @PostMapping("/api/v1/media/uploaded")
    public ResponseEntity<Void> uploaded(@Valid @RequestBody MediaUploadedRequest request,
                                          Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<MediaService.PartInfo> parts = request.parts() != null
                ? request.parts().stream()
                    .map(p -> new MediaService.PartInfo(p.partNumber(), p.etag()))
                    .toList()
                : null;
        mediaService.completeUpload(currentUser.getId(), request.mediaId(), parts);
        return ResponseEntity.ok().build();
    }
}
