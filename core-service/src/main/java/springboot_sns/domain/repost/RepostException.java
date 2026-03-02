package springboot_sns.domain.repost;

import com.pebble.springboot_sns.domain.common.DomainException;

public class RepostException extends DomainException {

    public RepostException(String message) {
        super(message);
    }
}
