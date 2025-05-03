package com.example.wigellkoncernbiluthyrning.exceptions;

public class CustomerHasOrdersException extends RuntimeException {
    public CustomerHasOrdersException(String message) {
        super(message);
    }
}
