package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.exceptions.CustomerHasOrdersException;
import com.example.wigellkoncernbiluthyrning.exceptions.CustomerNotFoundException;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.utility.SsnUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
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
        String rawPassword = SsnUtil.SsnToPassword(customer.getSocialSecurityNumber());

        customer.setPassword(rawPassword);
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
    public Customer updateCustomer(Customer customerDetails) {
        String loggedInCustomer = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!loggedInCustomer.equals(customerDetails.getSocialSecurityNumber())) {
            throw new AccessDeniedException("You are not authorized to update this");
        }

        CUSTOMER_LOGGER.info("Details for customer with id {} have been updated.", customerDetails.getId());
        Customer existingCustomer = customerRepository.findOptionalBySocialSecurityNumber(loggedInCustomer).orElseThrow(()-> new CustomerNotFoundException(loggedInCustomer));

        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setLastName(customerDetails.getLastName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhone(customerDetails.getPhone());

        return customerRepository.save(existingCustomer);

    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        if (!customer.getOrders().isEmpty()) {
            throw new CustomerHasOrdersException("Cannot delete customer with existing orders");
        }

        customerRepository.deleteById(id);
        CUSTOMER_LOGGER.warn("Customer with id {} was removed from the database", id);
    }

}
