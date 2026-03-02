package com.pebble.springboot_sns.domain.post;

import com.pebble.springboot_sns.domain.common.BaseEntity;
import com.pebble.springboot_sns.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "posts")
@Getter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 280)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long parentId;

    private Long quoteId;

    private Long repostId;

    @Column(nullable = false)
    private Integer repostCount = 0;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer replyCount = 0;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column(columnDefinition = "bigint[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<Long> mediaIds;

    protected Post() {
    }

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public Post(String content, User user, List<Long> mediaIds) {
        this.content = content;
        this.user = user;
        this.mediaIds = mediaIds;
    }

    public static Post createReply(String content, User user, Long parentId) {
        Post post = new Post(content, user);
        post.parentId = parentId;
        return post;
    }

    public static Post createQuote(String content, User user, Long quoteId) {
        Post post = new Post(content, user);
        post.quoteId = quoteId;
        return post;
    }

    public static Post createRepost(User user, Long repostId) {
        Post post = new Post("", user);
        post.repostId = repostId;
        return post;
    }

    public void incrementRepostCount() {
        this.repostCount++;
    }

    public void decrementRepostCount() {
        if (this.repostCount > 0) {
            this.repostCount--;
        }
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void incrementReplyCount() {
        this.replyCount++;
    }

    public void decrementReplyCount() {
        if (this.replyCount > 0) {
            this.replyCount--;
        }
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public boolean isReply() {
        return this.parentId != null;
    }

    public boolean isQuote() {
        return this.quoteId != null;
    }

    public boolean isRepost() {
        return this.repostId != null;
    }
}
