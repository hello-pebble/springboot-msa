package com.pebble.springboot_sns.domain.quote;

import com.pebble.springboot_sns.domain.common.DomainException;

public class QuoteException extends DomainException {

    public QuoteException(String message) {
        super(message);
    }
}
