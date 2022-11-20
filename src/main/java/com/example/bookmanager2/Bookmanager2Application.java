package com.example.bookmanager2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
public class Bookmanager2Application {

    public static void main(String[] args) {
        SpringApplication.run(Bookmanager2Application.class, args);
    }

}
