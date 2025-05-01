package com.example.wigellkoncernbiluthyrning.config;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;
    private final OrderRepository orderRepository;

    public DataInitializer(CustomerRepository customerRepository, CarRepository carRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        orderRepository.deleteAll();
        carRepository.deleteAll();
        customerRepository.deleteAll();


        Customer anna = new Customer("19850101-1234", "Anna", "Svensson", "anna@email.com", "0701111111");
        Customer erik = new Customer("19900215-5678", "Erik", "Johansson", "erik@email.com", "0702222222");
        Customer maria = new Customer("19751230-9101", "Maria", "Lindberg", "maria@email.com", "0703333333");
        Customer johan = new Customer("19881122-3456", "Johan", "Karlsson", "johan@email.com", "0704444444");
        Customer elin = new Customer("19950505-7890", "Elin", "Andersson", "elin@email.com", "0705555555");

        customerRepository.save(anna);
        customerRepository.save(erik);
        customerRepository.save(maria);
        customerRepository.save(johan);
        customerRepository.save(elin);

        Car car1 = new Car("Volvo", "XC60", "VOL123", 500, false, false);
        Car car2 = new Car("BMW", "320i", "BMW456", 700, false, false);
        Car car3 = new Car("Audi", "A4", "AUD789", 650, false, false);

        carRepository.save(car1);
        carRepository.save(car2);
        carRepository.save(car3);

        Order order1 = new Order();
        order1.setOrderPlaced(LocalDate.now().minusDays(10));
        order1.setStartDate(LocalDate.now().minusDays(9));
        order1.setReturnDate(LocalDate.now().minusDays(5));
        order1.setTotalCost(2500);
        order1.setCustomer(anna);
        order1.setCar(car1);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setOrderPlaced(LocalDate.now().minusDays(3));
        order2.setStartDate(LocalDate.now().minusDays(2));
        order2.setReturnDate(LocalDate.now().plusDays(2));
        order2.setTotalCost(2800);
        order2.setCustomer(erik);
        order2.setCar(car2);
        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setOrderPlaced(LocalDate.now());
        order3.setStartDate(LocalDate.now().plusDays(1));
        order3.setReturnDate(LocalDate.now().plusDays(4));
        order3.setTotalCost(1950);
        order3.setCustomer(maria);
        order3.setCar(car3);
        orderRepository.save(order3);

        System.out.println("✅ Sample data initialized!");
    }
}