package com.example.wigellkoncernbiluthyrning.integration;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Martin
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        carRepository.deleteAll();
        customerRepository.deleteAll();

        Customer anna = new Customer("19850101-1234", "Anna", "Svensson", "anna@example.com", "0701234567", "1234");
        customerRepository.save(anna);

        Car volvo = new Car("Volvo", "XC60", "VOL001", 500.0);
        Car toyota = new Car("Toyota", "Corolla", "TOY123", 300.0);
        Car bmw = new Car("BMW", "320i", "BMW789", 750.0);
        carRepository.saveAll(List.of(volvo, toyota, bmw));

        Order o1 = new Order();
        o1.setCustomer(anna);
        o1.setCar(volvo);
        o1.setStartDate(LocalDate.now().minusDays(5));
        o1.setReturnDate(LocalDate.now());
        o1.setTotalCost(2500);
        orderRepository.save(o1);

        Order o2 = new Order();
        o2.setCustomer(anna);
        o2.setCar(toyota);
        o2.setStartDate(LocalDate.of(2025, 3, 1));
        o2.setReturnDate(LocalDate.of(2025, 3, 4));
        o2.setOrderPlaced(LocalDate.of(2025, 3, 1));
        o2.setTotalCost(900);
        orderRepository.save(o2);

        Order o3 = new Order();
        o3.setCustomer(anna);
        o3.setCar(bmw);
        o3.setStartDate(LocalDate.now().minusDays(9));
        o3.setReturnDate(LocalDate.now().minusDays(1));
        o3.setTotalCost(6750);
        orderRepository.save(o3);

        Order o4 = new Order();
        o4.setCustomer(anna);
        o4.setCar(volvo);
        o4.setStartDate(LocalDate.now().minusDays(2));
        o4.setReturnDate(LocalDate.now());
        o4.setTotalCost(1500);
        orderRepository.save(o4);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanAccessStatistics() throws Exception {
        mockMvc.perform(get("/api/v1/admin/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mostRentedBrand").value("Volvo"))
                .andExpect(jsonPath("$.totalRevenue").value(2500 + 900 + 6750 + 1500))
                .andExpect(jsonPath("$.topCustomerBySpending.customer").value("19850101-1234 - Anna Svensson"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanAccessMonthlyStatistics() throws Exception {
        mockMvc.perform(get("/api/v1/admin/statistics/2025/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(900))
                .andExpect(jsonPath("$.totalOrdersInPeriod").value(1))
                .andExpect(jsonPath("$.totalIncomePerCar.TOY123").value(900));
    }

    @Test
    @WithMockUser(username = "19850101-1234", roles = {"CUSTOMER"})
    void customerCannotAccessStatistics() throws Exception {
        mockMvc.perform(get("/api/v1/admin/statistics"))
                .andExpect(status().isForbidden());
    }
}