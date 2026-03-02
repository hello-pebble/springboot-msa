package springboot_sns.domain.media;

import com.pebble.springboot_sns.domain.common.BaseEntity;
import com.pebble.springboot_sns.domain.media.MediaStatus;
import lombok.Getter;

@Entity
@Table(name = "media")
@Getter
public class Media extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;

    @Column(nullable = false)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaStatus status;

    @Column(nullable = false)
    private Long userId;

    private String uploadId;

    @Column(columnDefinition = "text")
    private String attributes;

    protected Media() {
    }

    public Media(MediaType mediaType, String path, MediaStatus status, Long userId) {
        this.mediaType = mediaType;
        this.path = path;
        this.status = status;
        this.userId = userId;
    }

    public Media(MediaType mediaType, String path, MediaStatus status, Long userId, String uploadId) {
        this(mediaType, path, status, userId);
        this.uploadId = uploadId;
    }

    public void updateStatus(MediaStatus status) {
        this.status = status;
    }
}
