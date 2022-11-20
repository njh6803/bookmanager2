package com.example.bookmanager2.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // 스프링에서 제공하는 별도의 기본 리스너 사용
public class JpaConfiguration {
}
