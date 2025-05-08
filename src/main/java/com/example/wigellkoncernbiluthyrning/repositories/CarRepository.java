package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//Anna, SCRUM-7

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {


    Car findCarById(Long id);

    Optional<Car> findByRegistrationNumber(String registrationNumber);

    @Query("SELECT c FROM Car c WHERE NOT EXISTS (" +
    "SELECT o FROM Order o WHERE o.car = c AND o.active = true AND o.startDate <= :today AND o.returnDate >= :today" +
    ")")
    List<Car> findAllAvailableCarsToday(@Param("today") LocalDate today);

}
