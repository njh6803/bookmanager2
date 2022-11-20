package com.example.bookmanager2.service;

import com.example.bookmanager2.domain.User;
import com.example.bookmanager2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class EntityManagerTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void entityManagerTest() {
        System.out.println(entityManager.createQuery("select u from User u").getResultList()); // userRepository.findAll()과 같은 쿼리를 동작시키는것
        // 하이버네이트에서 EntityManager를 세션이라고 부른다. SessionImpl 구현체에서 EntityManager를 구현하고 있다.

        // EntityManager는 캐시를 가지고 있어서 실제로 우리가 save() 메소드를 실행시키는 시점에 DB에 반영되지않는다.
        // 우리가 사용하는 영속성 컨텍스트와 실제 DB에 데이터의 갭이 발생한다는 의미이다.

    }

    @Test
    void cacheFindTest() {
        System.out.println(userRepository.findByEmail("martin@fastcampus.com"));
        System.out.println(userRepository.findByEmail("martin@fastcampus.com"));
        System.out.println(userRepository.findByEmail("martin@fastcampus.com"));
        //findByEmail sql문이 3번 호출된다.

        System.out.println(userRepository.findById(2L).get());
        System.out.println(userRepository.findById(2L).get());
        System.out.println(userRepository.findById(2L).get());
        // findById sql문이 3번 호출된다.
        // @Transactional을 선언을 해서 하나의 트랜잭션으로 실행을 시키게되면 sql문이 1번만 호출된다.
        // 즉 조회시에 영속성 컨텍스트에 존재하는 엔티티 캐시에서 직접 처리한 것을 알 수 있다. (진짜 DB의 쿼리를 조회하지않는다.)
        // 우리가 따로 캐시 설정을 하지않았지만 영속성 컨텍스트 내에서 자동으로 엔티티에 대해서 캐시처리하는것을 일반적으로 JPA의 1차 캐시라고 이야기한다.
        // findByEmail과 findById의 1차캐시가 적용되고 적용안된 차이점이 무엇일까
        // 1차캐시는 맵의 형태로 만들어지고 키는 아이디값 벨류는 해당 엔티티가 들어있다.
        // 그래서 아이디로 조회하면 영속성 컨텍스트 내에 존재하는 1차캐시에 엔티티가 있는지 확인해보고 있으면 DB조회없이 바로 값을 리턴해주고
        // 만약에 1차캐시 내에 엔티티가 존재하지 않는다면 실제 쿼리로 DB에 조회를 해서 결과값을 1차캐시 한번 저장을 하고 리턴해준다.
        // findByEmail는 아이디 값이 아니기 때문에 3번 호출된다.
        // 1차캐시가 적용됨에따라 jpa 조회성능이 올라간다. 개발자가 직접 아이디값 활용해서 조회하는 경우는 드물지만 JPA 특성상 기본적으로 아이디값을 활용한 조회가 매우 빈번하게 일어난다.
        // 그래서 해당 로직에 대한 성능 저하가 일부 생길 수 있는데 1차캐시를 활용함으로써 그런 성능 저하를 줄이기 위한 대책으로 생각된다.


        // deleteById를 하게 되면 select문으로 아이디값을 한번 조회하고 delete문이 실행된다. 하지만 1차캐시를 이용하게 되면 조회문이 일어나지않는다.
        // 하나의 트랜잭션안에서 동작할 때에는 1차캐시를 이용하여 동작함으로써 성능저하를 방지하도록 되어있다.
        // @Transactional은 1차캐시을 역할은 한다. 영속성 컨텍스트가 존재함에 따라서 JPA의 특징중 하나인 지연쓰기가 발생한다.
        // 실제로 @Transactional이 존재하는상태에서 테스트를 실행하게되면 select문만 존재하고 delete문이 존재하지않는다.
        // @Transactional내에서는 최대한 DB에 반영하는 시간을 늦추는 처리때문이다. 영속성 컨텍스트내에서 엔티티매니저가 자체적으로 엔티티 상태를 머지를 하고
        // 최종적으로 DB에 반영하는 내용에 대해서만 쿼리가 실행된다. 그래서 이 경우에 delete쿼리가 실행되지않은 이유는
        // 최종 커밋이 되지않고 이 경우는 테스트이기때문에 롤백 트랜잭션을 했기때문에 해당 delete쿼리가 영속성 컨텍스트 내에서만 존재하고 실제로 DB에 반영되지않는다.
        userRepository.deleteById(1L);
    }

    @Test
    void cacheFindTest2() {
        // @Transactional 제거하고 실행
        // 트랜잭션이 선언이 안되어 있기 때문에 update쿼리가 그때 그때 반영하는 것이 되므로 2번 실행된다.
        // 사실 save() 메소드 내부에 @Transactional 존재하지만 상위 @Transactional 존재하지않으면 내부에서만 트랜잭션을 한번 묶어준다는 의미이다.
        // 즉 @Transactional 묶지않으면 save() 메소드 각각이 하나의 트랜잭션이 된다.

        // @Transactional 선언하고 실행
        // 엔티티 캐시를 통해서 지연쓰기가 일어나기때문에 영속성 컨텍스트 내에서 각각의 변경내용을 가지고있다가 머지를 해서 1번의 update로 모두 처리하게 되어있다.
        // 그래서 2번의 업데이트 처리해야하는것을 엔티티 캐시 쪽에서 2개의 쿼리를 머지를 하고 업데이트문은 한번만 실행된다.

        // 영속성 컨텍스트내의 캐시를 잘 이해하게 되면 flush()의 역할도 이해할 수 있게 된다.
        // flush의 뜻은 모여있는 것을 비워낸다는 의미이다.
        // 즉 영속성 컨텍스트에 쌓여있는 데이터는 엔티티매니저가 자체적으로 DB에 영속화를 해주지만 사실 개발자가 의도한 타이밍에 DB에 영속화가 일어나지않는다.
        // 개발자가 원하는 시점에 영속화를 하기위해 flush()메소드를 사용하게 된다.
        // flush()를 남발하면 엔티티 캐시의 장점을 모두 무효화시키므로 적당하게 잘 사용하는 것이 중요하다.

        // 영속성 컨텍스트와 DB가 동기화되는 시점은 언제인가
        // flush()가 호출하는순간
        // 트랜잭션이 종료되는순간
        // 아이디값이 아닌 JPQL쿼리가 실행될 때
        //      - 예를 들어 userRepository.findAll()을 실행하면 영속성 컨텍스트와 실제 DB와 비교하여 변경 부분을 반영해줘야하는데 merge하기가 쉽지않기때문에
        //                 영속성 캐시에 있던 값을 모두 flush()를 시키고 DB에 다 반영한 후에 다시 DB에서 조회해서 영속성 컨텍스트로 가져온다.

        User user = userRepository.findById(1L).get();
        user.setName("marrrrrtin");

        userRepository.save(user);

        System.out.println("===================================");

        user.setEmail("marrrrrtin@fastcampus.com");

        userRepository.save(user);

        // userRepository.flush();

        System.out.println(">>> 1 : " + userRepository.findById(1L).get());

        userRepository.flush();

        System.out.println(">>> 2 : " + userRepository.findById(1L).get());
    }

    @Test
    void cacheFindTest3() {
        // 영속성 컨텍스트 내에서 EntityManager가 엔티티의 상태를 어떻게 변화시키는지 알아보자
        /*
            EntityManager 생명주기
            - 비영속 상태 = 영속성 컨텍스트가 해당 엔티티 객체를 관리하지않는 상태 - @Trasiant, new 상태
            - 영속 상태 = 해당 엔티티가 영속성 컨텍스트 관리하에 존재하는 상태, 영속성 컨텍스트에서 관리된다고 한다면 객체의 변화를 별도로 처리해주지 않더라도
                         DB에 반영시켜주게 된다.
            - 준영속 상태 = detached 상태. 원래 영속화되어있던 객체를 분리해서 영속성 컨텍스트 밖으로 꺼낸 상태
            - 삭제 상태 - remove 상태.
        */
    }
}
