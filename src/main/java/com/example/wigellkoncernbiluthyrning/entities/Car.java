package com.example.wigellkoncernbiluthyrning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

//Anna, SCRUM-6

@Entity
@Table (name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "The car needs to have a make")
    @Column(name = "make", nullable = false)
    private String make;
    @NotBlank(message = "The car needs to have a model")
    @Column(name = "model", nullable = false)
    private String model;
    @NotBlank(message = "The car needs to have a registration number")
    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;
    @Column(name = "price_per_day")
    private double pricePerDay;

    private boolean booked;
    private boolean inService;

    @OneToMany(mappedBy = "car")
    private List<Order> orders;

    public Car() {}

    public Car(String make, String model, String registrationNumber, double pricePerDay, boolean booked, boolean inService) {
        this.make = make;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.pricePerDay = pricePerDay;
        this.booked = booked;
        this.inService = inService;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    public double getPricePerDay() {
        return pricePerDay;
    }
    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
    public boolean isBooked() {
        return booked;
    }
    public void setBooked(boolean booked) {
        this.booked = booked;
    }
    public boolean isInService() {
        return inService;
    }
    public void setInService(boolean inService) {
        this.inService = inService;
    }

}

