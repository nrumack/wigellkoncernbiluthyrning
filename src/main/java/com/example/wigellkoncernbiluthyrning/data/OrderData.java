package com.example.wigellkoncernbiluthyrning.data;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

//Anna SCRUM-45, SCRUM-46

@org.springframework.core.annotation.Order(2)
@Component
@DependsOn({"carData", "customerData"})
public class OrderData implements CommandLineRunner {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;

    public OrderData(OrderRepository orderRepository, CustomerRepository customerRepository, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) {
        if (orderRepository.count() == 0) {
            Optional<Customer> anna = customerRepository.findOptionalBySocialSecurityNumber("19850101-1234");
            Optional<Customer> erik = customerRepository.findOptionalBySocialSecurityNumber("19900215-5678");
            Optional<Customer> maria = customerRepository.findOptionalBySocialSecurityNumber("19751230-9101");
            Optional<Customer> johan = customerRepository.findOptionalBySocialSecurityNumber("19881122-3456");
            Optional<Customer> elin = customerRepository.findOptionalBySocialSecurityNumber("19950505-7890");


            Optional<Car> car1 = carRepository.findByRegistrationNumber("ABC123");
            Optional<Car> car2 = carRepository.findByRegistrationNumber("XYZ789");
            Optional<Car> car3 = carRepository.findByRegistrationNumber("LMN456");
            Optional<Car> car4 = carRepository.findByRegistrationNumber("QWE987");
            Optional<Car> car5 = carRepository.findByRegistrationNumber("TES123");


            if (anna.isPresent() && car1.isPresent()) {
                createOrder(anna.get(), car1.get(), LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 16));
            }
            if (erik.isPresent() && car2.isPresent()) {
                createOrder(erik.get(), car2.get(), LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 17));
            }
            if (maria.isPresent() && car3.isPresent()) {
                createOrder(maria.get(), car3.get(), LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 18));
            }
            if (johan.isPresent() && car4.isPresent()) {
                createOrder(johan.get(), car4.get(), LocalDate.of(2025, 5, 12), LocalDate.of(2025, 5, 16));
            }
            if (elin.isPresent() && car5.isPresent()) {
                createOrder(elin.get(), car5.get(), LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 17));
            }

            if (anna.isPresent() && car1.isPresent()) {
                createOrder(anna.get(), car1.get(), LocalDate.of(2025, 3, 12), LocalDate.of(2025, 3, 17));
            }
            if (johan.isPresent() && car5.isPresent()) {
                createOrder(johan.get(), car5.get(), LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 3));
            }
            if (anna.isPresent() && car2.isPresent()) {
                createOrder(anna.get(), car2.get(), LocalDate.of(2025, 5, 7), LocalDate.of(2025, 5, 9));
            }
            if (maria.isPresent() && car3.isPresent()) {
                createOrder(maria.get(), car3.get(), LocalDate.of(2025, 3, 13), LocalDate.of(2025, 3, 16));
            }
            if (erik.isPresent() && car3.isPresent()) {
                createOrder(erik.get(), car3.get(), LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 6));
            }
            if (anna.isPresent() && car1.isPresent()) {
                createOrder(anna.get(), car1.get(), LocalDate.of(2025, 5, 6), LocalDate.of(2025, 5, 8));
            }
            if (elin.isPresent() && car2.isPresent()) {
                createOrder(elin.get(), car2.get(), LocalDate.of(2025, 5, 4), LocalDate.of(2025, 5, 8));
            }
            if (maria.isPresent() && car4.isPresent()) {
                createOrder(maria.get(), car4.get(), LocalDate.of(2025, 5, 4), LocalDate.of(2025, 5, 5));
            }
            if (johan.isPresent() && car1.isPresent()) {
                createOrder(johan.get(), car1.get(), LocalDate.of(2025, 5, 4), LocalDate.of(2025, 5, 8));
            }

        }
    }

    private void createOrder(Customer customer, Car car, LocalDate startDate, LocalDate returnDate) {
        long days = ChronoUnit.DAYS.between(startDate, returnDate) + 1;
        double cost = car.getPricePerDay() * days;
    
        Order order = new Order();

        order.setOrderPlaced(startDate.minusDays(1));
        order.setCustomer(customer);
        order.setCar(car);
        order.setStartDate(startDate);
        order.setReturnDate(returnDate);
        order.setTotalCost((int) cost);
   
        orderRepository.save(order);
    }
    
}
