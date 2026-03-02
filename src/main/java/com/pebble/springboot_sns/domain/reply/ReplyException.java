package com.pebble.springboot_sns.domain.reply;

import com.pebble.springboot_sns.domain.common.DomainException;

public class ReplyException extends DomainException {

    public ReplyException(String message) {
        super(message);
    }
}
