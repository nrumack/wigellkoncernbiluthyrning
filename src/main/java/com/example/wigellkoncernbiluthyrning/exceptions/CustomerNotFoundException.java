package com.example.wigellkoncernbiluthyrning.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String SocialSecurityNumber) {
        super("Customer with Social Security number " + SocialSecurityNumber + " not found");
    }

    public CustomerNotFoundException(Long id) {
        super("Customer with id " + id + " not found");
    }
}
