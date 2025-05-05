package com.example.wigellkoncernbiluthyrning.exceptions;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(Long id) {
        super("Car with id: " + id + " not found");
    }
}
