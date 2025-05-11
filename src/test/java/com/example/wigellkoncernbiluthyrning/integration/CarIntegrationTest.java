package com.example.wigellkoncernbiluthyrning.integration;

import com.example.wigellkoncernbiluthyrning.entities.Car;
import com.example.wigellkoncernbiluthyrning.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Anna SCRUM-42

@SpringBootTest
@AutoConfigureMockMvc
public class CarIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanAddCar() throws Exception {
        String json = """
            {
              "make": "Volvo",
              "model": "XC90",
              "registrationNumber": "VOL999",
              "pricePerDay": 600.0,
              "booked": false
            }
        """;

        mockMvc.perform(post("/api/v1/admin/addcar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber", is("VOL999")));
    }

    @Test
    @WithMockUser(username = "19850101-1234", roles = {"CUSTOMER"})
    void customerCanViewAvailableCars() throws Exception {
        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanRemoveCar() throws Exception {
        Car car = new Car("Toyota", "Yaris", "TOY999", 450.0);
        car = carRepository.save(car);

        mockMvc.perform(delete("/api/v1/admin/removecar/" + car.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Car removed successfully"));
    }

    @Test
    @WithMockUser(username = "19850101-1234", roles = {"CUSTOMER"})
    void customerCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/removecar/1"))
                .andExpect(status().isForbidden());
    }
}