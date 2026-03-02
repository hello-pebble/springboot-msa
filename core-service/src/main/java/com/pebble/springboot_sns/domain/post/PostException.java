package com.pebble.springboot_sns.domain.post;

import com.pebble.springboot_sns.domain.common.DomainException;

public class PostException extends DomainException {

    public PostException(String message) {
        super(message);
    }
}
