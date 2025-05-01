package com.example.wigellkoncernbiluthyrning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        UserDetails anna = User.withUsername("19850101-1234")
                .password("1234")
                .roles("CUSTOMER")
                .build();

        UserDetails erik = User.withUsername("19900215-5678")
                .password("5678")
                .roles("CUSTOMER")
                .build();

        UserDetails maria = User.withUsername("19751230-9101")
                .password("9101")
                .roles("CUSTOMER")
                .build();

        UserDetails johan = User.withUsername("19881122-3456")
                .password("3456")
                .roles("CUSTOMER")
                .build();

        UserDetails elin = User.withUsername("19950505-7890")
                .password("7890")
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, anna, erik, maria, johan, elin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .headers().frameOptions().disable() 
                .and()
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