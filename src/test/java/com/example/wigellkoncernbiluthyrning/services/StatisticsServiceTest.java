package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


// Martin
class StatisticsServiceTest {

    private StatisticsService statisticsService;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        statisticsService = new StatisticsService(orderRepository);
    }

    @Test
    void testMostRentedBrand() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now()));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();

        assertThat(stats.get("mostRentedBrand")).isEqualTo("Volvo");
    }

    @Test
    void testMostRentedModel() {
        List<Order> orders = List.of(createOrder("Volvo", "XC60", LocalDate.now()));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();

        assertThat(stats.get("mostRentedModel")).isEqualTo("XC60");
    }

    @Test
    void testAverageRentalLength() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 100, 5));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();

        assertThat(stats.get("averageRentalLengthInDays")).isEqualTo(5.0);
    }

    @Test
    void testLongestRentalDuration() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 100, 7));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();

        assertThat(stats.get("longestRentalDurationInDays")).isEqualTo(7);
    }

    @Test
    void testTotalOrdersThisYear() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now()));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();

        assertThat(stats.get("totalOrdersThisYear")).isEqualTo(1L);
    }

    @Test
    void testAverageOrderCost() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 200));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();

        assertThat(stats.get("averageOrderCost")).isEqualTo(200.0);
    }

    @Test
    void testTotalRevenue() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 500), createOrder("Volvo", "V70", LocalDate.now(), 700));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();

        assertThat(stats.get("totalRevenue")).isEqualTo(1200);
    }

    @Test
    void testIncomePerCar() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 100));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();
        @SuppressWarnings("unchecked")
        Map<String, Integer> incomePerCar = (Map<String, Integer>) stats.get("incomePerCar");

        assertThat(incomePerCar.get("ABC123")).isEqualTo(100);
    }

    @Test
    void testIncomePerBrand() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 300));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();
        @SuppressWarnings("unchecked")
        Map<String, Integer> incomePerBrand = (Map<String, Integer>) stats.get("incomePerBrand");

        assertThat(incomePerBrand.get("Volvo")).isEqualTo(300);
    }

    @Test
    void testMonthlyRevenueLast12Months() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 500));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();
        @SuppressWarnings("unchecked")
        Map<String, Integer> monthlyRevenue = (Map<String, Integer>) stats.get("monthlyRevenueLast12Months");

        YearMonth current = YearMonth.now();
        assertThat(monthlyRevenue.get(current.toString())).isEqualTo(500);
    }

    @Test
    void testTopCustomerBySpending() {
        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 1000));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatistics();

        @SuppressWarnings("unchecked")
        Map<String, Object> topCustomer = (Map<String, Object>) stats.get("topCustomerBySpending");

        assertThat(topCustomer.get("totalSpent")).isEqualTo(1000);
    }

    @Test
    void testGetStatisticsForPeriodTotalIncome() {
        LocalDate from = LocalDate.now().minusDays(10);
        LocalDate to = LocalDate.now().plusDays(10);

        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 300));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatisticsForPeriod(from, to);

        assertThat(stats.get("totalIncome")).isEqualTo(300);
    }

    @Test
    void testGetStatisticsForPeriodAverageOrderCost() {
        LocalDate from = LocalDate.now().minusDays(10);
        LocalDate to = LocalDate.now().plusDays(10);

        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 400));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatisticsForPeriod(from, to);

        assertThat(stats.get("averageOrderCost")).isEqualTo(400.0);
    }

    @Test
    void testGetStatisticsForPeriodTotalOrders() {
        LocalDate from = LocalDate.now().minusDays(10);
        LocalDate to = LocalDate.now().plusDays(10);

        List<Order> orders = List.of(createOrder("Volvo", "V50", LocalDate.now(), 300));
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, Object> stats = statisticsService.getStatisticsForPeriod(from, to);

        assertThat(stats.get("totalOrdersInPeriod")).isEqualTo(1);
    }

    private Order createOrder(String make, String model, LocalDate placedDate) {
        return createOrder(make, model, placedDate, 1000);
    }

    private Order createOrder(String make, String model, LocalDate placedDate, int cost) {
        return createOrder(make, model, placedDate, cost, 3);
    }

    private Order createOrder(String make, String model, LocalDate placedDate, int cost, int rentalDays) {
        Car car = new Car();
        car.setMake(make);
        car.setModel(model);
        car.setRegistrationNumber("ABC123");

        Customer customer = new Customer();
        customer.setSocialSecurityNumber("19800101-1234");
        customer.setFirstName("Test");
        customer.setLastName("Customer");

        Order order = new Order();
        order.setCar(car);
        order.setCustomer(customer);
        order.setOrderPlaced(placedDate);
        order.setStartDate(placedDate);
        order.setReturnDate(placedDate.plusDays(rentalDays));
        order.setTotalCost(cost);

        return order;
    }
}
