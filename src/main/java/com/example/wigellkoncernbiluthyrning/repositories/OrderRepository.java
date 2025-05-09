package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Marc
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByReturnDateGreaterThanEqual(LocalDate currentDate);

    List<Order> findOrderByReturnDateBefore(LocalDate currentDate);

    void deleteById(@NonNull Long id);

    void deleteOrderByReturnDateBefore(LocalDate givenDate);

    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.car = :car AND o.startDate <= :today AND o.returnDate >= :today AND o.active = true")
    boolean isCarBookedToday(@Param("car") Car car, @Param("today") LocalDate today);


    @Query("""
    SELECT COUNT(o) > 0 
    FROM Order o 
    WHERE o.car = :car 
      AND o.active = true 
      AND o.returnDate > :startDate 
      AND o.startDate < :returnDate
    """)
    boolean isCarBookedDuringPeriod(@Param("car") Car car, 
                                    @Param("startDate") LocalDate startDate, 
                                    @Param("returnDate") LocalDate returnDate);


}
