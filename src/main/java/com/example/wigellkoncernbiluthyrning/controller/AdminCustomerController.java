package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.services.CustomerService;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//Martin
@RestController
@RequestMapping("/api/v1/admin")
public class AdminCustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    @Autowired
    public AdminCustomerController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping("/addcustomer")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<Customer> existingCustomer = customerRepository.findOptionalBySocialSecurityNumber(customer.getSocialSecurityNumber());
        if (existingCustomer.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Social Security Number", "A person with the social security number already exists"));
        }
        return ResponseEntity.ok(customerService.saveCustomer(customer));
    }

    @DeleteMapping("/removecustomer/{id}")
    public ResponseEntity<String> removeCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer removed successfully");
    }
}