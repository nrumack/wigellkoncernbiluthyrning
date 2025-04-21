package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepositories extends JpaRepository<Customer,Long> {
}
