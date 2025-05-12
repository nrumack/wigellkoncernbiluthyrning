package com.example.wigellkoncernbiluthyrning.repositories;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import com.example.wigellkoncernbiluthyrning.services.OrderServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Marc
@ExtendWith({MockitoExtension.class})
class OrderRepositoryTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Principal mockPrincipal;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private final LocalDate tomorrow = LocalDate.now().plusDays(1);
    private final LocalDate today = LocalDate.now();
    private final LocalDate yesterday = LocalDate.now().minusDays(1);
    private final LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void activeOrders_ReturnAllActiveOrders() {

        // Given
        mockPrincipal = () -> "19201101-8899";

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

        Order order3 = new Order();
        order3.setReturnDate(tomorrow);
        order3.setCustomer(customer);
        order3.setActive(false);

        when(orderRepository.findByReturnDateGreaterThanEqual(today)).thenReturn(List.of(order1, order2, order3));

        // When
        List<Order> result = orderServiceImpl.activeOrders(today, mockPrincipal);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository).findByReturnDateGreaterThanEqual(any(LocalDate.class));
    }

    @Test
    void pastOrders_ADMIN_ReturnAllOrders() {

        mockPrincipal = () -> "testCredentials";

        Order order1 = new Order();
        Order order2 = new Order();
        when(orderRepository.findOrderByReturnDateBefore(any())).thenReturn(List.of(order1, order2));

        Authentication authentication = new TestingAuthenticationToken(
                null, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<Order> allPastOrders = orderServiceImpl.pastOrders(today, mockPrincipal);

        assertEquals(2, allPastOrders.size());
        assertEquals(List.of(order1, order2), allPastOrders);
    }

    @Test
    void pastOrders_CUSTOMER_ReturnCustomerPastOrders() {

        mockPrincipal = () -> "matches";

        Customer matchingCustomer = new Customer();
        matchingCustomer.setSocialSecurityNumber("matches");
        Customer nonMatchingCustomer = new Customer();
        nonMatchingCustomer.setSocialSecurityNumber("doesNotMatch");

        Order order1 = new Order();
        order1.setCustomer(matchingCustomer);

        Order order2 = new Order();
        order2.setCustomer(matchingCustomer);

        Order order3 = new Order();
        order3.setCustomer(nonMatchingCustomer);

        when(orderRepository.findOrderByReturnDateBefore(any())).thenReturn(List.of(order1, order2, order3));

        Authentication authentication = new TestingAuthenticationToken(
                null, null, List.of(new SimpleGrantedAuthority("CUSTOMER")));

        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<Order> matchingOrders = orderServiceImpl.pastOrders(today, mockPrincipal);

        assertEquals(2, matchingOrders.size());
    }

    @Test
    void deleteById_DeletesById() {
        // Given
        Long orderId = 1L;
        // When
        orderRepository.deleteById(orderId);
        // Then
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void deleteOrderByReturnDateBefore_DeletesAllOrdersBeforeDateToday() {

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
