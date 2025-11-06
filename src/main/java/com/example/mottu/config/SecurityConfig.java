package com.example.mottu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    @Order(1) // PRIORIDADE MÁXIMA
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        return http
                // Corresponde APENAS a requisições que começam com /api/
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())

                // Configurações RESTful: stateless, sem formLogin, sem redirecionamento
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // Endpoint de login da API
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        // Endpoint /users (assumindo que é /api/users para a API)
                        // Se /users for da API, ajuste para: .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                        // Caso contrário, será tratado pela webSecurity
                        .anyRequest().authenticated()
                )

                // Tratamento de exceção para retornar 401 em vez de redirecionar
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(401, "Unauthorized: Token Invalido ou Ausente"))
                )

                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2) // PRIORIDADE MENOR (Geral)
    SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        return http

                .authorizeHttpRequests(auth -> auth
                        // Permite acesso a recursos estáticos, cadastro e Swagger
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/usuarios-view/cadastro").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        // Libera endpoints Web que devem ser públicos (ex: /users que você liberou anteriormente)
                        .requestMatchers(HttpMethod.GET, "/users").permitAll()
                        // Qualquer outra requisição web deve estar autenticada
                        .anyRequest().authenticated()
                )

                // Configuração de login para o Thymeleaf (faz o redirecionamento)
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/motos-view")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}