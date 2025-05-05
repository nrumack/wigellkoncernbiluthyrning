package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findBySocialSecurityNumber(String socialSecurityNumber);
    Optional<Customer> findOptionalBySocialSecurityNumber(String ssn);


    List<Customer> id(long id);
}
