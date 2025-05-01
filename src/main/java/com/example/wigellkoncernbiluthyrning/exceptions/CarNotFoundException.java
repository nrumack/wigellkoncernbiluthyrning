package com.example.wigellkoncernbiluthyrning.exceptions;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException() {
        super("No cars available currently");
    }
}
