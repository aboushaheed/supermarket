package com.supermarket.shop.exception;

import lombok.Getter;

import java.util.List;

import static java.util.Collections.singletonList;

@Getter
public class BadParameterException extends RuntimeException {

    public BadParameterException(String message) {
        super(message);
    }
}
