package com.example.bookmanager2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 스프링에서 제공하는 별도의 기본 리스너 사용
@SpringBootApplication
public class Bookmanager2Application {

    public static void main(String[] args) {
        SpringApplication.run(Bookmanager2Application.class, args);
    }

}
