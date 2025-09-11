package com.example.mottu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MottuApplication {

    public static void main(String[] args) {
        SpringApplication.run(MottuApplication.class, args);
    }

}
