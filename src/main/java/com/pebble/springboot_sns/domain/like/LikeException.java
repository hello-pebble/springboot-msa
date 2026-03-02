package com.pebble.springboot_sns.domain.like;

import com.pebble.springboot_sns.domain.common.DomainException;

public class LikeException extends DomainException {

    public LikeException(String message) {
        super(message);
    }
}
