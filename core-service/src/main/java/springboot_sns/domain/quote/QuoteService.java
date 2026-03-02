package springboot_sns.domain.quote;

import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostRepository;
import com.pebble.springboot_sns.domain.quote.QuoteException;
import com.pebble.springboot_sns.domain.quote.QuoteRepository;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post create(String content, Long userId, Long quoteId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new QuoteException("사용자를 찾을 수 없습니다."));

        postRepository.findByIdAndDeletedAtIsNull(quoteId)
                .orElseThrow(() -> new QuoteException("인용할 게시글을 찾을 수 없습니다."));

        postRepository.incrementRepostCount(quoteId);

        Post quote = Post.createQuote(content, user, quoteId);
        return quoteRepository.save(quote);
    }

    public List<Post> findByQuoteId(Long quoteId) {
        postRepository.findByIdAndDeletedAtIsNull(quoteId)
                .orElseThrow(() -> new QuoteException("게시글을 찾을 수 없습니다."));

        return quoteRepository.findByQuoteIdAndDeletedAtIsNull(quoteId);
    }

    @Transactional
    public void delete(Long quotePostId, Long userId) {
        Post quotePost = postRepository.findByIdAndDeletedAtIsNull(quotePostId)
                .orElseThrow(() -> new QuoteException("인용 게시글을 찾을 수 없습니다."));

        if (!quotePost.isQuote()) {
            throw new QuoteException("인용 게시글이 아닙니다.");
        }

        if (!quotePost.getUser().getId().equals(userId)) {
            throw new QuoteException("본인의 인용 게시글만 삭제할 수 있습니다.");
        }

        if (quotePost.getQuoteId() != null) {
            postRepository.decrementRepostCount(quotePost.getQuoteId());
        }

        quotePost.delete();
    }
}
