package com.example.wigellkoncernbiluthyrning.services;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.exceptions.CarDeleteException;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import com.example.wigellkoncernbiluthyrning.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private CarRepository carRepository;
    private OrderRepository orderRepository;
    private OrderService orderService;
    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderService = mock(OrderService.class);
        carService = new CarServiceImpl(carRepository, orderRepository, orderService);
    }

    @Test
    void testGetAvailableCarsToday_onlyReturnsUnbookedCars() {
        Car car1 = new Car("Volvo", "XC60", "ABC123", 500.0);
        car1.setId(1L);
        Car car2 = new Car("Toyota", "Corolla", "XYZ789", 400.0);
        car2.setId(2L);

        List<Car> allCars = Arrays.asList(car1, car2);
        when(carRepository.findAll()).thenReturn(allCars);
        when(orderRepository.isCarBookedToday(car1, LocalDate.now())).thenReturn(false);
        when(orderRepository.isCarBookedToday(car2, LocalDate.now())).thenReturn(true);

        List<Car> result = carService.getAvailableCarsToday();

        assertEquals(1, result.size());
        assertEquals("ABC123", result.get(0).getRegistrationNumber());
    }

    @Test
    void testAddCar() {
        Car car = new Car("Tesla", "Model 3", "TES123", 699.0);
        when(carRepository.save(car)).thenReturn(car);

        Car saved = carService.addCar(car);
        assertEquals("TES123", saved.getRegistrationNumber());
        verify(carRepository).save(car);
    }

    @Test
    void testRemoveCar_throwsIfBooked() {
        Car car = new Car("BMW", "520i", "BMW520", 700.0);
        car.setId(99L);
        when(carRepository.findById(99L)).thenReturn(java.util.Optional.of(car));
        when(orderRepository.isCarBookedToday(eq(car), any())).thenReturn(true);

        assertThrows(CarDeleteException.class, () -> carService.removeCar(99L));
    }

    @Test
    void testRemoveCar_success() {
        Car car = new Car("Ford", "Focus", "FOC456", 300.0);
        car.setId(55L);
        when(carRepository.findById(55L)).thenReturn(java.util.Optional.of(car));
        when(orderRepository.isCarBookedToday(eq(car), any())).thenReturn(false);

        carService.removeCar(55L);
        verify(carRepository).deleteById(55L);
    }
}
