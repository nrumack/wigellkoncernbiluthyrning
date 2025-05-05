package com.example.wigellkoncernbiluthyrning.controller;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public CustomerController(CustomerRepository customerRepository, CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @PutMapping("/updateinfo/{id}")
    public ResponseEntity<Customer> updateInfo(@PathVariable Long id, @RequestBody Customer customerDetails) {

        customerDetails.setId(id);
        Customer updatedCustomer = customerService.updateCustomer(customerDetails);
        return ResponseEntity.ok(updatedCustomer);

    }
}