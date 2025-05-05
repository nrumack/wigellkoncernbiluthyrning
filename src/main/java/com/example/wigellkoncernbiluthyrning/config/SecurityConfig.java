package com.example.wigellkoncernbiluthyrning.config;

import com.example.wigellkoncernbiluthyrning.repositories.CustomerRepository;
import com.example.wigellkoncernbiluthyrning.entities.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomerRepository customerRepository;

    public SecurityConfig(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (username.equals("admin")) {
                return User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN")
                        .build();
            }

            Customer customer = customerRepository.findBySocialSecurityNumber(username);

            if (customer == null) {
                throw new UsernameNotFoundException("User not found");
            }
            
            return User.withUsername(customer.getSocialSecurityNumber())
                    .password(customer.getPassword())
                    .roles("CUSTOMER")
                    .build();

        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions().disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()
            )
            .httpBasic();

        return http.build();
    }
}