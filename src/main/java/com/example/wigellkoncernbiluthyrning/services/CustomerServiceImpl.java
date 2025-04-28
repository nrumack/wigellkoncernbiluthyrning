package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private static final Logger CUSTOMER_LOGGER = LogManager.getLogger(CustomerServiceImpl.class);

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        CUSTOMER_LOGGER.info("{} {} was added to the database.", customer.getFirstName(), customer.getLastName());
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        CUSTOMER_LOGGER.info("Details for customer with id {} have been updated.", customer.getId());
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        CUSTOMER_LOGGER.warn("Customer with id {} was removed from the database", id);
        customerRepository.deleteById(id);
    }

}
