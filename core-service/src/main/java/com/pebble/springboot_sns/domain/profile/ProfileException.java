package com.pebble.springboot_sns.domain.profile;

import com.pebble.springboot_sns.domain.common.DomainException;

public class ProfileException extends DomainException {

    public ProfileException(String message) {
        super(message);
    }
}
