package springboot_sns.domain.user;

import com.pebble.springboot_sns.domain.common.DomainException;

public class UserException extends DomainException {

    public UserException(String message) {
        super(message);
    }
}
