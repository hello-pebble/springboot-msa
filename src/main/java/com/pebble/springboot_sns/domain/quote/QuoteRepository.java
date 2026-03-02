package com.pebble.springboot_sns.domain.quote;

import com.pebble.springboot_sns.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Post, Long> {

    List<Post> findByQuoteIdAndDeletedAtIsNull(Long quoteId);
}
