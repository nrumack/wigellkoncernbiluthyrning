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
            if (anna.isPresent() && car1.isPresent()) {
                createOrder(anna.get(), car1.get(), LocalDate.of(2024, 1, 5), LocalDate.of(2024, 1, 10));
            }
            if (erik.isPresent() && car2.isPresent()) {
                createOrder(erik.get(), car2.get(), LocalDate.of(2024, 2, 10), LocalDate.of(2024, 2, 11));
            }
            if (maria.isPresent() && car3.isPresent()) {
                createOrder(maria.get(), car3.get(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 6));
            }
            if (anna.isPresent() && car2.isPresent()) {
                createOrder(anna.get(), car2.get(), LocalDate.of(2024, 4, 3), LocalDate.of(2024, 4, 10));
            }
            if (erik.isPresent() && car1.isPresent()) {
                createOrder(erik.get(), car1.get(), LocalDate.of(2024, 5, 10), LocalDate.of(2024, 5, 12));
            }
            if (maria.isPresent() && car1.isPresent()) {
                createOrder(maria.get(), car1.get(), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 4));
            }

            if (anna.isPresent() && car4.isPresent()) {
                createOrder(anna.get(), car4.get(), LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
            }
            if (erik.isPresent() && car5.isPresent()) {
                createOrder(erik.get(), car5.get(), LocalDate.now().minusDays(1), LocalDate.now().plusDays(4));
            }
            if (maria.isPresent() && car3.isPresent()) {
                createOrder(maria.get(), car3.get(), LocalDate.now().minusDays(3), LocalDate.now().plusDays(1));
            }

            if (anna.isPresent() && car2.isPresent()) {
                createOrder(anna.get(), car2.get(), LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));
            }
            if (erik.isPresent() && car4.isPresent()) {
                createOrder(erik.get(), car4.get(), LocalDate.now().plusDays(4), LocalDate.now().plusDays(9));
            }
            if (maria.isPresent() && car5.isPresent()) {
                createOrder(maria.get(), car5.get(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
            }
            if (anna.isPresent() && car1.isPresent()) {
                createOrder(anna.get(), car1.get(), LocalDate.now().plusDays(10), LocalDate.now().plusDays(13));
            }

        }
    }

    private void createOrder(Customer customer, Car car, LocalDate startDate, LocalDate returnDate) {
        long days = ChronoUnit.DAYS.between(startDate, returnDate) + 1;
        double cost = car.getPricePerDay() * days;
    
        Order order = new Order();
        order.setOrderPlaced(LocalDate.of(2025,1,1));
        order.setCustomer(customer);
        order.setCar(car);
        order.setStartDate(startDate);
        order.setReturnDate(returnDate);
        order.setTotalCost((int) cost);
   
        orderRepository.save(order);
    }
    
}
