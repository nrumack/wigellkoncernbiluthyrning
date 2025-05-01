package com.example.wigellkoncernbiluthyrning.exceptions;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) {
        super("no cars available for " + message);
    }
}
