package com.ilario.sparkmart.security;

import com.ilario.sparkmart.security.misc.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                .requestMatchers("/users/**").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers("/addresses/**").hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers("/brands/**").hasAnyRole(Role.ADMINISTRATOR.name(), Role.EMPLOYEE.name())
                .requestMatchers("/categories/**").hasAnyRole(Role.ADMINISTRATOR.name(), Role.EMPLOYEE.name())
                .requestMatchers("/products/**").hasAnyRole(Role.ADMINISTRATOR.name(), Role.EMPLOYEE.name())
                .requestMatchers("/customer/**").hasAnyRole(Role.ADMINISTRATOR.name(), Role.EMPLOYEE.name(), Role.CUSTOMER.name())
                .anyRequest().authenticated()
                .and()
                .logout().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
//http
//                .csrf()
//                .disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .logout().permitAll()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//