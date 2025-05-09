package com.example.wigellkoncernbiluthyrning.repositories;

import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.services.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Marc
@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private final LocalDate tomorrow = LocalDate.now().plusDays(1);
    private final LocalDate today = LocalDate.now();
    private final LocalDate yesterday = LocalDate.now().minusDays(1);
    private final LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

    @Test
    void findByReturnDateGreaterThanEqual() {

        // Given
        Principal principal = () -> "19201101-8899";

        Customer customer = new Customer();
        customer.setSocialSecurityNumber("19201101-8899");

        Order order1 = new Order();
        order1.setReturnDate(today);
        order1.setCustomer(customer);
        order1.setActive(true);

        Order order2 = new Order();
        order2.setReturnDate(tomorrow);
        order2.setCustomer(customer);
        order2.setActive(true);

        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findByReturnDateGreaterThanEqual(today)).thenReturn(orders);

        // When
        List<Order> result = orderServiceImpl.activeOrders(today, principal);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository).findByReturnDateGreaterThanEqual(any(LocalDate.class));
    }

    @Test
    void findOrderByReturnDateBefore() {

        // Given
        Principal principal = () -> "19201101-8899";

        Customer customer = new Customer();
        customer.setSocialSecurityNumber("19201101-8899");

        Order order1 = new Order();
        order1.setReturnDate(yesterday);
        order1.setCustomer(customer);

        Order order2 = new Order();
        order2.setReturnDate(twoDaysAgo);
        order2.setCustomer(customer);

        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findOrderByReturnDateBefore(today)).thenReturn(orders);

        // When
        orderServiceImpl.pastOrders(today, principal);

        // Then
        verify(orderRepository).findOrderByReturnDateBefore(any(LocalDate.class));
    }

    @Test
    void deleteById() {
        // Given
        Long orderId = 1L;
        // When
        orderRepository.deleteById(orderId);
        // Then
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void deleteOrderByReturnDateBefore() {

        // Given
        Order order1 = new Order();
        order1.setReturnDate(yesterday);

        Order order2 = new Order();
        order2.setReturnDate(twoDaysAgo);

        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findOrderByReturnDateBefore(today)).thenReturn(orders);
        doNothing().when(orderRepository).deleteOrderByReturnDateBefore(any(LocalDate.class));

        // When
        orderServiceImpl.deleteOrdersBeforeGivenDate(today);

        // Then
        verify(orderRepository).findOrderByReturnDateBefore(any(LocalDate.class));
        verify(orderRepository).deleteOrderByReturnDateBefore(any(LocalDate.class));
    }
}
