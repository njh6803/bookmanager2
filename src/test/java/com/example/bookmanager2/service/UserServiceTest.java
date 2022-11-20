package com.example.bookmanager2.service;

import com.example.bookmanager2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void test() {
        userService.put(); // 비영속 상태, new 상태

        // System.out.println(">>> " + userRepository.findByEmail("newUser@fastcampus.com"));
        userRepository.findAll().forEach(System.out::println);
    }
}