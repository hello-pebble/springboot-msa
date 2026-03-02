package com.pebble.springboot_sns.domain.follow;

import com.pebble.springboot_sns.domain.common.DomainException;

public class FollowException extends DomainException {

    public FollowException(String message) {
        super(message);
    }
}
