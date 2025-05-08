package com.example.wigellkoncernbiluthyrning.data;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//Anna, scrum-27

@org.springframework.core.annotation.Order(0)
@Component
public class CustomerData implements CommandLineRunner {
    private final CustomerRepository customerRepository;

    public CustomerData(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0) {
            customerRepository.save(new Customer("19850101-1234", "Anna", "Svensson", "anna@example.com", "0701234567", "1234"));
            customerRepository.save(new Customer("19900215-5678", "Erik", "Johansson", "erik@example.com", "0702345678", "5678"));
            customerRepository.save(new Customer("19751230-9101", "Maria", "Lindberg", "maria@example.com", "0703456789", "9101"));
            customerRepository.save(new Customer("19881122-3456", "Johan", "Karlsson", "johan@example.com", "0704567890", "3456"));
            customerRepository.save(new Customer("19950505-7890", "Elin", "Andersson", "elin@example.com", "0705678901", "7890"));

        }
    }

//    private void addCustomer(String ssn, String firstName, String lastName, String email, String phone) {
//        String password = ssn.substring(ssn.length() - 4);
//        customerRepository.save(new Customer(ssn, firstName, lastName, email, phone, password));
//    }
}
