package com.example.wigellkoncernbiluthyrning.validators;

import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

// Marc
@Service
public class OrderValidator {
    OrderRepository orderRepository;
    private boolean isValid;

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

        /*if (order.getCustomer().getSocialSecurityNumber() == null || order.getCustomer().getSocialSecurityNumber().isBlank()) {
            isValid = false;
            return "You need to enter your social security number";
        }*/

        /*if (order.getCustomer().getFirstName() == null || order.getCustomer().getFirstName().isBlank()) {
            isValid = false;
            return "You need to enter your first name";
        }

        if (order.getCustomer().getLastName() == null || order.getCustomer().getLastName().isBlank()) {
            isValid = false;
            return "You need to enter your last name";
        }*/

        if (order.getCar().getId() == null) {
            isValid = false;
            return "You have not selected a car";
        }

        isValid = true;
        return null;
    }

    public boolean isValid() {
        return isValid;
    }
}
