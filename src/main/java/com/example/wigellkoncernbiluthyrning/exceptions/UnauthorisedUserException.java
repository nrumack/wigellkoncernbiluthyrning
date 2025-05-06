package com.example.wigellkoncernbiluthyrning.exceptions;

public class UnauthorisedUserException extends RuntimeException {
    public UnauthorisedUserException(String message) {
        super(message);
    }
}
