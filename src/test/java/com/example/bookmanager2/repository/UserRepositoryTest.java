package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.Gender;
import com.example.bookmanager2.domain.User;
import com.example.bookmanager2.domain.UserHistory;
import org.assertj.core.util.Lists;
import org.hibernate.annotations.Fetch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserHistoryRepository userHistoryRepository;

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
    @Transactional()
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
    void crud4() {
        User user = userRepository.findById(1L).orElse(null);  // Optional로 랩핑되어있기 때문에 별도의 처리가 필요하다.

        System.out.println(user);
        // findById()에서는 실제로 em.find()를 통해서 엔티티 객체를 직접 가져오고있다.(Eager Fecth 방식을 사용)
    }

    @Test
    void crud5() {
        userRepository.save(new User("new martin", "newmartin@fastcampus.com"));

        userRepository.flush(); // DB 반영 시점을 조절하는 메소드 로그상에 큰 변화는 확인해 볼 수 없다

        userRepository.saveAndFlush(new User("new martin2", "newmartin2@fastcampus.com"));

        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    void crud6() {
        //long count = userRepository.count();
        boolean exists = userRepository.existsById(1L);

        //System.out.println(count);
        System.out.println(exists);
    }

    @Test
    void crud7() {
        // userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new)); // delete 메소드는 null값일 될 수 없다.
        // 내가 작성한 select 쿼리문 이외의 한번 더 select 쿼리를 조회한다.

        userRepository.deleteById(1L);
        // 조회문 한번과 delele문 한번 실행된다.
        // delete문 실행전에 값이 있는지 확인하기 위해 조회문을 실행한다.

    }

    @Test
    void crud8() {
        // userRepository.deleteAll(userRepository.findAllById(Lists.newArrayList(1L, 3L))); // 실제 서비스에서 실행할 일은 거의 없다.
        // findAll과 비슷하게 성능이슈가 발생한다.
        // select문의로 한번 findAll()로 조회 후 객체 수 만큼 delete문 실행
        // 파라미터를 줄 경우에는 select문으로 한번 조회 후 존재하는지 확인하는 조회문이 객체 수 만큼 실행 된후 객채 수 만큼 delete문 실행

        //userRepository.deleteInBatch(userRepository.findAllById(Lists.newArrayList(1L, 3L)));
        // delete전에 체크하는 select문이 존재하지 않고 delete문에 or 조건으로 한번에 삭제한다.

        userRepository.deleteAllInBatch();
        // 파라미터 값을 넘겨주지 않으면 delete전에 체크하는 select문이 존재하지 않고 delete문으로 조건없이 한번에 삭제한다.
        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    void crud9() {
        Page<User> users = userRepository.findAll(PageRequest.of(0, 3));
        // 제로베이스 인덱스라서 우리가 생각하는 1페이지가 0이다.

        System.out.println("page : " + users);
        System.out.println("totalElements : " + users.getTotalElements()); // 전체 객체 수
        System.out.println("totalPages : " + users.getTotalPages()); // 전체 페이지 수
        System.out.println("numberOfElements : " + users.getNumberOfElements()); // 현재 가져온 객체 수
        System.out.println("sort : " + users.getSort());
        System.out.println("size : " + users.getSize());

        users.getContent().forEach(System.out::println);
    }

    // queryByExample qbe로 축약해서 부르기도한다.
    // 엔티티를 example로 만들고 matcher를 추가해서 선언해 줌으로써 필요한 쿼리들을 만드는 방법이다.
    // 최근에 나온 방식
    // 검색이 필요한 인자를 example이라는것으로 추가해 줌으로써 쿼리를 생성할 수 있다.
    // example 자체가 matcher에 대한 제약이 일부있어서 특히나 문자열에 관련된 것들만 쓸 수 있는 한계점이 존재한다.
    // 조금 복잡한 쿼리를 만들게되면 일반적으로는 example을 쓰지 않고 QueryDSL과 같은 별도의 방법으로 구현하게된다.
    // 실제로는 생각처럼 많이 쓰이지는 않는다.
    @Test
    void crud10() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name") // 제외시키는 메소드, name은 매칭하지 않겠다는 의미
                .withMatcher("email", endsWith()); // email은 확인하겠다는 의미  // endsWith() Like 검색
        Example<User> example = Example.of(new User("ma", "fastcampus.com"), matcher); // Probe로 엔티티를 넣어주면 되는데, Probe는 실제로 엔티티는 아니라는 의미이다.

        userRepository.findAll(example).forEach(System.out::println);
    }

    @Test
    void crud11() {
        User user = new User();
        user.setEmail("slow");

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("email", contains()); // 양방향 Like 검색
        Example<User> example = Example.of(user, matcher);

        userRepository.findAll(example).forEach(System.out::println);
    }

    @Test
    void crud12() {
        userRepository.save(new User("david", "david@fastcampus.com"));

        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setEmail("martin-update@fastcampus.com");

        userRepository.save(user);
        // SimpleJpaRepository  JpaRepository의 구현체
    }

    @Test
    void select() {
        System.out.println(userRepository.findByName("dennis"));
        // sout이라고 치면 인텔리제이 기본 템플릿을 이용해서 System.out.println()을 찍을 수 있다.
        // ctrl + d - 복사
        System.out.println("findByEmail : " + userRepository.findByEmail("martin@fastcampus.com"));
        System.out.println("getByEmail : " + userRepository.getByEmail("martin@fastcampus.com"));
        System.out.println("readByEmail : " + userRepository.readByEmail("martin@fastcampus.com"));
        System.out.println("queryByEmail : " + userRepository.queryByEmail("martin@fastcampus.com"));
        System.out.println("searchByEmail : " + userRepository.searchByEmail("martin@fastcampus.com"));
        System.out.println("streamByEmail : " + userRepository.streamByEmail("martin@fastcampus.com"));
        System.out.println("findUserByEmail : " + userRepository.findUserByEmail("martin@fastcampus.com"));
        // Something 해당 문자열 자체가 구현체에서 무시됨을 알 수 있다.
        // Spring jpa data에서 자유도를 제공하는 만큼 혹시라도 잘못된 정보를 네이밍에 사용하지않도록 잘 코딩을 해야한다는것을 의미한다.
        System.out.println("findSomethingByEmail : " + userRepository.findSomethingByEmail("martin@fastcampus.com"));
        // 모두 동일한 쿼리와 동일한 결과를 가진다.
        // 코드 가독성에 가장 잘어울리는 이름을 골라서 사용
        System.out.println("findTop1ByName : " + userRepository.findTop1ByName("martin"));
        System.out.println("findFirstByName : " + userRepository.findFirstByName("martin"));
        System.out.println("findTop2ByName : " + userRepository.findTop2ByName("martin"));
        System.out.println("findFirst2ByName : " + userRepository.findFirst2ByName("martin"));
        System.out.println("findLast1ByName : " + userRepository.findLast1ByName("martin"));

        System.out.println("findByEmailAndName : " + userRepository.findByEmailAndName("martin@fastcampus.com","martin"));
    }

    @Test
    void select2() {
        System.out.println("findByEmailAndName : " + userRepository.findByEmailAndName("martin@fastcampus.com","martin"));
        System.out.println("findByEmailOrName : " + userRepository.findByEmailOrName("martin@fastcampus.com","dennis"));

        System.out.println("findByCreatedAtAfter : " + userRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByIdAfter : " + userRepository.findByIdAfter(4L));
        System.out.println("findByCreatedAtGreaterThan : " + userRepository.findByCreatedAtGreaterThan(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByCreatedAtGreaterThanEqual : " + userRepository.findByCreatedAtGreaterThanEqual(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByCreatedAtBetween : " + userRepository.findByCreatedAtBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L)));
        System.out.println("findByIdBetween : " + userRepository.findByIdBetween(1L, 3L));

        System.out.println("findByIdIsNotNull : " + userRepository.findByIdIsNotNull());
        // System.out.println("findByIdIsNotEmpty : " + userRepository.findByAddressIsNotEmpty());  // Collection type을 비교한다.

        // in절 안에 리스트가 너무 많으면 성능적인 이슈를 발생시키기때문에 항상 in절에 데이터가 어느정도 들어가는지 사전에 검토하고 사용하는 것이 좋다.
        System.out.println("findByNameIn : " + userRepository.findByNameIn(Lists.newArrayList("margin", "dennis")));

        System.out.println("findByNameStartingWith : " + userRepository.findByNameStartingWith("mar"));
        System.out.println("findByNameEndingWith : " + userRepository.findByNameEndingWith("tin"));
        System.out.println("findByNameContains : " + userRepository.findByNameContains("art"));
        System.out.println("findByNameLike : " + userRepository.findByNameLike("%art%")); // 위 3개의 메소드는 Like를 한번 랩핑한 것
    }

    @Test
    void pagingAndSortingTest() {
        System.out.println("findTop1ByName : " + userRepository.findTop1ByName("martin"));
        System.out.println("findLast1ByName : " + userRepository.findLast1ByName("martin"));
        System.out.println("findTop1ByNameOrderByIdDesc : " + userRepository.findTop1ByNameOrderByIdDesc("martin"));
        System.out.println("findFirstByNameOrderByIdDescEmailAsc : " + userRepository.findFirstByNameOrderByIdDescEmailAsc("martin"));  // 메소드명이 너무 길어지면 가독성이 떨어진다.
        System.out.println("findFirstByNameWithSortParams : " + userRepository.findFirstByName("martin", Sort.by(Sort.Order.desc("id"), Sort.Order.asc("email"))));
        System.out.println("findFirstByNameWithSortParams : " + userRepository.findFirstByName("martin", getSort()));
        // 네이밍 기반을 사용할 지 Sort기반을 사용할 지 적절하게 사용

        System.out.println("findByNameWithPaging : " + userRepository.findByName("martin", PageRequest.of(0, 1,Sort.by(Sort.Order.desc("id")))).getContent());
    }

    private Sort getSort() {
        return Sort.by(
                Sort.Order.desc("id"),
                Sort.Order.asc("email")
        );
    }

    @Test
    void insertAndUpdateTest(){
        User user = new User();
        user.setName("노지형");
        user.setEmail("njh4803@nate.com");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("노노노노노노지형");

        userRepository.save(user2);
    }

    @Test
    void enumTest() {
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setGender(Gender.MALE);

        userRepository.save(user);

        userRepository.findAll().forEach(System.out::println);

        System.out.println(userRepository.findRawRecord().get("gender")); // @Enumerated(value = EnumType.STRING) 을 안한 경우에는 값이 0이 출력된다. ENUM Gender의 MALE, FEMALE 순서를 바꾸면 1이 나온다.
    }

    @Test
    void listenerTest() {
        User user = new User();
        user.setEmail("njh4803@naver.com");
        user.setName("노지형");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("노지노지노지형");

        userRepository.save(user2);

        userRepository.deleteById(4L);
    }

    @Test
    void prePersistTest() {
        User user = new User();
        user.setEmail("njh4803@naver.com");
        user.setName("노지형");
        /*user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());*/

        userRepository.save(user);

        System.out.println(userRepository.findByEmail("njh4803@naver.com"));
    }

    @Test
    void preUpdateTest() {
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);

        System.out.println("as-is : " + user);

        user.setName("노지형");
        userRepository.save(user);

        System.out.println("to-be : " + userRepository.findAll().get(0));
    }

    @Test
    void userHistoryTest() {
        User user = new User();
        user.setEmail("njh4803@naver.com");
        user.setName("노지형");

        userRepository.save(user);

        user.setName("노지형노지형노지형");

        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);
        // NullPointException 발생
        // 엔티티 리스너는 스프링 빈을 주입받지 못한다.
        // 스프링 빈을 주입할 수 있는 특별한 클래스를 만들어준다. support패키지에 BeanUtils 만들어서 이용
    }

    @Test
    void userRelationTest() {
        User user = new User();
        user.setName("david");
        user.setEmail("david@fastcampus.com");
        user.setGender(Gender.MALE);

        userRepository.save(user);

        user.setName("daniel");

        userRepository.save(user);

        user.setEmail("daniel@fastcampus.com");

        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);

        /*List<UserHistory> result = userHistoryRepository.findByUserId(
                userRepository.findByEmail("daniel@fastcampus.com").getId());*/

        List<UserHistory> result = userRepository.findByEmail("daniel@fastcampus.com").getUserHistories();
        // LazyInitializationException 발생 - @OneToMany 에 (fetch = FetchType.EAGER)을 설정

        result.forEach(System.out::println);
        System.out.println("UserHistory.getUser() : " + userHistoryRepository.findAll().get(0).getUser());
    }

    @Test
    void userRelationTest2() {
    }
}