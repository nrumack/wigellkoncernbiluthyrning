package com.example.wigellkoncernbiluthyrning.services.calculators;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


// Marc
@ExtendWith(MockitoExtension.class)
class OrderCalculatorTest {
    OrderCalculator orderCalculator;

    @Mock
    Car testCar;

    @Mock
    Order testOrder;

    @BeforeEach
    void setUp() {
        orderCalculator = new OrderCalculator();
    }

    @Test
    void calculateTotalCost() {

        // Given
        double pricePerDay = 500;
        LocalDate startDate = LocalDate.now();
        LocalDate returnDate = LocalDate.now().plusDays(9);

        when(testOrder.getStartDate()).thenReturn(startDate);
        when(testOrder.getReturnDate()).thenReturn(returnDate);
        when(testCar.getPricePerDay()).thenReturn(pricePerDay);
        when(testOrder.getTotalCost()).thenReturn(5000);

        // When
        orderCalculator.calculateTotalCost(testOrder, testCar);

        // Then
        verify(testOrder).getStartDate();
        verify(testOrder).getReturnDate();
        verify(testCar).getPricePerDay();
        verify(testOrder).setTotalCost(anyInt());

        assertEquals(5000, testOrder.getTotalCost());
    }
}