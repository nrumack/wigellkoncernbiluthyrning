package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Anna, SCRUM-7

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByBookedFalseAndInServiceFalse();

    Car findCarById(Long id);

    Optional<Car> findByRegistrationNumber(String registrationNumber);
}
