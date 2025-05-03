package com.example.wigellkoncernbiluthyrning.exceptions;

public class AvailableCarNotFoundException extends RuntimeException {
    public AvailableCarNotFoundException() {
        super("No cars available currently");
    }
}
