package com.example.wigellkoncernbiluthyrning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// Martin
@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails anna = User.withUsername("19850101-1234")
                .password(passwordEncoder.encode("1234"))
                .roles("CUSTOMER")
                .build();

        UserDetails erik = User.withUsername("19900215-5678")
                .password(passwordEncoder.encode("5678"))
                .roles("CUSTOMER")
                .build();

        UserDetails maria = User.withUsername("19751230-9101")
                .password(passwordEncoder.encode("9101"))
                .roles("CUSTOMER")
                .build();

        UserDetails johan = User.withUsername("19881122-3456")
                .password(passwordEncoder.encode("3456"))
                .roles("CUSTOMER")
                .build();

        UserDetails elin = User.withUsername("19950505-7890")
                .password(passwordEncoder.encode("7890"))
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, anna, erik, maria, johan, elin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
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
