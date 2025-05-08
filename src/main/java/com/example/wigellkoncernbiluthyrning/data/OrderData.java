package com.example.wigellkoncernbiluthyrning.data;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

//Anna SCRUM-45

@Component
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
                createOrder(anna.get(), car1.get(), LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
            }
            if (erik.isPresent() && car2.isPresent()) {
                createOrder(erik.get(), car2.get(), LocalDate.now().minusDays(3), LocalDate.now());
            }
            if (maria.isPresent() && car3.isPresent()) {
                createOrder(maria.get(), car3.get(), LocalDate.now().minusDays(9), LocalDate.now().minusDays(8));
            }

            if (johan.isPresent() && car4.isPresent()) {
                createOrder(johan.get(), car4.get(), LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));
            }

            if (elin.isPresent() && car5.isPresent()) {
                createOrder(elin.get(), car5.get(), LocalDate.now().minusDays(3), LocalDate.now());
            }
        }
    }

    private void createOrder(Customer customer, Car car, LocalDate startDate, LocalDate returnDate) {
        long days = ChronoUnit.DAYS.between(startDate, returnDate);
        double cost = car.getPricePerDay() * days;
    
        Order order = new Order();
        order.setCustomer(customer);
        order.setCar(car);
        order.setStartDate(startDate);
        order.setReturnDate(returnDate);
        order.setTotalCost((int) cost);
    
        order.setActive(!startDate.isAfter(LocalDate.now()) && !returnDate.isBefore(LocalDate.now()));
    
        orderRepository.save(order);
    
        car.setBooked(true);
        carRepository.save(car);
    }
}
