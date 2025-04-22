package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Anna, SCRUM-7

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // Hämta alla tillgängliga bilar
    List<Car> findByBookedFalseAndInServiceFalse();

    // Hämta bil via registreringsnummer, ska vi ha den metoden?
    Car findByRegistrationNumber(String registrationNumber);
}
