package com.example.mottu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthFilter authFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios-view/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios-view/cadastro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/motos-view/**", "/alas-view/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/motos-view/admin/**", "/alas-view/admin/**", "/users-view/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login/**").permitAll()
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/motos-view")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout"))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
