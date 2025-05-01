package com.example.wigellkoncernbiluthyrning.validators;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderValidator {
    private boolean isValid;

    public String validateDates(Order order) {

        if (order.getStartDate() == null) {
            isValid = false;
            return "Start date cannot be null";
        }

        if (order.getReturnDate() == null) {
            isValid = false;
            return "Return date cannot be null";
        }

        if (order.getStartDate().isBefore(LocalDate.now())) {
            isValid = false;
            return "The start date cannot be in the past";
        }

        if (order.getStartDate().isAfter(order.getReturnDate())) {
            isValid = false;
            return "The start date cannot be after the return date";
        }

        isValid = true;
        return null;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
