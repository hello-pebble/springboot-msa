package com.pebble.springboot_sns.domain.media;

import com.pebble.springboot_sns.domain.common.DomainException;

public class MediaException extends DomainException {

    public MediaException(String message) {
        super(message);
    }
}
