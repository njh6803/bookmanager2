package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.User;
import org.assertj.core.util.Lists;
import org.hibernate.annotations.Fetch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void crud() {   // Create Read Update Delete
        userRepository.save(new User());

        /*for (User user : userRepository.findAll()) {
            System.out.println(user);
        }*/
        // 위 내용과 동일
        userRepository.findAll().forEach(System.out::println); // 자바8에서 제공하는 Stream을 람다식으로 이용하여 출력

        // findAll() 실제 서비스에서는 성능적인 이슈가 있어서 잘 사용하지 않는다.
        // 회원정보가 천만명정도된다고 한다면 전체데이터를 리스트로 받아서 힙메모리 영역에 저장한다고한다면 outOfMemory가 날것이다.

        // flush() - 현재 jpa context에서 가지고있는 db값을 db에 반영하도록 지시하는 메소드
        // saveAndFlush() - 저장한 데이터를 jpa context에서 가지고있지말고 바로 db에 반영해서 적용할것을 지시하는 메소드
        // deleteInBatch() - 엔티티를 리스트 형식으로 받아서 한번에 지우는 메소드
        // deleteAllInBatch() - 조건없이 해당 엔티티, 테이블을 다 지우는 메소드, 실제 서비스에서는 쓸일이 없다.
        // getOne() - id로 하나만 가져오는 메소드
        // findById() - getOne()과 비슷하지만 Optional 객체로 랩핑을 해서 리턴해주는 메소드, getOne() 과는 동작이 조금 다르다.
        // existsById() - 해당 객체가 존재하는지 여부를 확인하는 메소드
        // count() - 전체 엔티티의 개수를 가져올 수 있다. 페이징 시 활용

    }

    @Test
    void crud2() {
        //List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        //List<User> users2 = userRepository.findAllById(Lists.newArrayList(1L, 3L, 5L)); // test용으로 제공

        // users.forEach(System.out::println);
        //users2.forEach(System.out::println);

        User user1 = new User("jack", "jack@fastcampus");
        User user2 = new User("steve", "steve@fastcampus");

        userRepository.saveAll(Lists.newArrayList(user1, user2));

        List<User> users = userRepository.findAll();

        users.forEach(System.out::println);
    }

    @Test
    void crud3() {
        User user = userRepository.getOne(1L);

        System.out.println(user);
        // 오류발생 could not initialize proxy [com.example.bookmanager2.domain.User#1] - no Session
        // Session은 flush에서 쓰는 jpa context의 개념
        // getOne()을 하고 System.out.println(user);까지 세션을 유지시켜주려면 @Transactional을 선언해주면 세션이 유지된다.
        // getOne()은 기본적으로 엔티티에 대해서 Lazy한 로딩을 지원하고있다. (Lazy Fecth를 지원하고있다.)
        // getOne()의 경우에는 구현체가 em.getReference(getDomainClass(), id)로 도메인 클래스를 가져온다. (이게 프록시를 의미한다.)
        // Lazy Fecth를 할때에는 getReference를 통해서 레퍼런스만 가지고 있고 실제 값을 구하는 시점에 세션을 통해서 조회를 해가지고오게된다.
    }

    @Test
    @Transactional()
    void crud4() {
        User user = userRepository.findById(1L).orElse(null);  // Optional로 랩핑되어있기 때문에 별도의 처리가 필요하다.

        System.out.println(user);
        // findById()에서는 실제로 em.find()를 통해서 엔티티 객체를 직접 가져오고있다.(Eager Fecth 방식을 사용)
    }
}