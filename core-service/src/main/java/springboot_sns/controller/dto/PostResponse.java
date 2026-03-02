package springboot_sns.controller.dto;

import com.pebble.springboot_sns.domain.post.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String content,
        Long userId,
        String username,
        Long parentId,
        Long quoteId,
        Long repostId,
        Integer repostCount,
        Integer likeCount,
        Integer replyCount,
        Long viewCount,
        boolean isLikedByMe,
        boolean isRepostedByMe,
        QuotedPost quotedPost,
        RepostedPost repostedPost,
        List<Long> mediaIds,
        LocalDateTime createdAt
) {
    public record QuotedPost(
            Long id,
            String content,
            Long userId,
            String username,
            List<Long> mediaIds
    ) {
        public static QuotedPost from(Post original) {
            return new QuotedPost(
                    original.getId(),
                    original.getContent(),
                    original.getUser().getId(),
                    original.getUser().getUsername(),
                    original.getMediaIds()
            );
        }
    }

    public static PostResponse from(Post post) {
        return from(post, false, false);
    }

    public static PostResponse from(Post post, boolean isLikedByMe) {
        return from(post, isLikedByMe, false);
    }

    public static PostResponse from(Post post, boolean isLikedByMe, boolean isRepostedByMe) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getParentId(),
                post.getQuoteId(),
                post.getRepostId(),
                post.getRepostCount(),
                post.getLikeCount(),
                post.getReplyCount(),
                post.getViewCount(),
                isLikedByMe,
                isRepostedByMe,
                null,
                null,
                post.getMediaIds(),
                post.getCreatedAt()
        );
    }

    public record RepostedPost(
            Long id,
            String content,
            Long userId,
            String username,
            List<Long> mediaIds
    ) {
        public static RepostedPost from(Post original) {
            return new RepostedPost(
                    original.getId(),
                    original.getContent(),
                    original.getUser().getId(),
                    original.getUser().getUsername(),
                    original.getMediaIds()
            );
        }
    }

    public static PostResponse fromRepost(Post repost, Post original, boolean isLikedByMe, boolean isRepostedByMe) {
        return new PostResponse(
                repost.getId(),
                repost.getContent(),
                repost.getUser().getId(),
                repost.getUser().getUsername(),
                repost.getParentId(),
                repost.getQuoteId(),
                repost.getRepostId(),
                repost.getRepostCount(),
                repost.getLikeCount(),
                repost.getReplyCount(),
                repost.getViewCount(),
                isLikedByMe,
                isRepostedByMe,
                null,
                RepostedPost.from(original),
                repost.getMediaIds(),
                repost.getCreatedAt()
        );
    }

    public static PostResponse fromQuote(Post quote, Post original, boolean isLikedByMe, boolean isRepostedByMe) {
        return new PostResponse(
                quote.getId(),
                quote.getContent(),
                quote.getUser().getId(),
                quote.getUser().getUsername(),
                quote.getParentId(),
                quote.getQuoteId(),
                quote.getRepostId(),
                quote.getRepostCount(),
                quote.getLikeCount(),
                quote.getReplyCount(),
                quote.getViewCount(),
                isLikedByMe,
                isRepostedByMe,
                QuotedPost.from(original),
                null,
                quote.getMediaIds(),
                quote.getCreatedAt()
        );
    }
}
