package com.example.bookmanager2.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// 자바의 클래스는 모두 Object를 상속받는다.
class UserTest {

    @Test
    void test() {
        User user = new User();
        user.setEmail("njh4803@gmail.com");
        user.setName("노지형");

        User user2 = User.builder().name("노지형").email("njh4803@gmail.com").build();

        System.out.println(">>>> " + user); // 암묵적으로 Object의 toString()을 사용
    }
}