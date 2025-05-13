package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//Nicholas
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void getCustomerById() {

            Long id = 1L;
            Customer mockCustomer = new Customer();
            mockCustomer.setId(id);
            mockCustomer.setFirstName("John");
            mockCustomer.setLastName("Smith");
            mockCustomer.setEmail("john.smith@gmail.com");
            mockCustomer.setPhone("123456789");
            mockCustomer.setPassword("password");

            when(customerRepository.findById(id)).thenReturn(Optional.of(mockCustomer));

        Customer result = customerService.getCustomerById(id);

            assertNotNull(result);
            assertEquals("John", result.getFirstName());


    }
}