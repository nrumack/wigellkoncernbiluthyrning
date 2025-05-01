package com.example.wigellkoncernbiluthyrning.entities;

import jakarta.persistence.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.ArrayList;
import java.util.List;

//Created by Nicholas
@Entity
@Table (name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique=true, nullable=false)
    private String socialSecurityNumber;
    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable=false)
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable=false)
    private String lastName;
    @NotBlank(message = "Email is required")
    @Column(name = "email")
    private String email;
    @NotBlank(message = "phonenumber is required")
    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    public Customer() {}

    public Customer(String socialSecurityNumber, String firstName, String lastName, String email, String phone) {
        this.socialSecurityNumber = socialSecurityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
