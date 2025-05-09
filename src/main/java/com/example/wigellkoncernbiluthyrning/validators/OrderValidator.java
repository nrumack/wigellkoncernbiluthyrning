package com.example.wigellkoncernbiluthyrning.validators;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.exceptions.IncorrectFormException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

// Marc
@Service
public class OrderValidator {
    private boolean isValid;

    public void validateOrder(Order order) {
        String message = validateOrderEntries(order);
        if (!isValid()) {
            throw new IncorrectFormException(message);
        }
    }

    public String validateOrderEntries(Order order) {

        if (order.getStartDate() == null || order.getStartDate().toString().isBlank()) {
            isValid = false;
            return "You need to enter a start date";
        }

        if (order.getReturnDate() == null || order.getReturnDate().toString().isBlank()) {
            isValid = false;
            return "You need to enter a return date";
        }

        if (order.getStartDate().isBefore(LocalDate.now())) {
            isValid = false;
            return "The start date cannot be in the past";
        }

        if (order.getReturnDate().isBefore(LocalDate.now())) {
            isValid = false;
            return "The return date cannot be in the past";
        }

        if (order.getStartDate().isAfter(order.getReturnDate())) {
            isValid = false;
            return "The start date cannot be after the return date";
        }

        if (order.getCar().getId() == null) {
            isValid = false;
            return "You have not selected a car";
        }

        if (order.getCustomer().getId() == null) {
            isValid = false;
            return "You have not selected a customer";
        }

        isValid = true;
        return null;
    }

    public boolean isValid() {
        return isValid;
    }
}
