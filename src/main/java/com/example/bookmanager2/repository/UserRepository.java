package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

// JpaRepository - PagingAndSortingRepository와 QueryByExampleExecutor를 상속받는다.
// QueryByExampleExecutor - Example객체를 활용한 메소드들을 제공
// PagingAndSortingRepository - CrudRepository를 상속받는다. Pageable를 이용하여 페이징을 도와주는 기능을 내장하고있다.
// CrudRepository - 우리가 사용하는 대부분의 기본 메소드들은 여기에 정의되어있다.
public interface UserRepository extends JpaRepository<User/* Entity */, Long /* Id 값*/> {
    // List<User> findByName(String name);
    // Optional<User> findByName(String name);
    Set<User> findByName(String name); // return type을 무수히 많이 제공해준다.
    Set<User> findByNameIs(String names);   // Is = Equal = 생략가능, 코드 가독성을 위해 제공되는 키워드이다.
    Set<User> findUserByName(String names);
    Set<User> findUserByNameEquals(String names); // 모두 동일
    User findByEmail(String email);

    User getByEmail(String email);

    User readByEmail(String email);

    User queryByEmail(String email);

    User searchByEmail(String email);

    User streamByEmail(String email);

    User findUserByEmail(String email);

    User findSomethingByEmail(String email);

    User findFirstByName(String name);
    User findTop1ByName(String name);

    List<User> findFirst2ByName(String name);
    List<User> findTop2ByName(String name);
    List<User> findLast1ByName(String name); // Last1은 없고 order by문으로 역순으로 가져오게된다.

    List<User> findByEmailAndName(String email, String name);
    List<User> findByEmailOrName(String email, String name);

    List<User> findByCreatedAtAfter(LocalDateTime yesterday);

    List<User> findByIdAfter(Long id); // 가독성을 위해서 after before는 시간에 쓴다

    List<User> findByCreatedAtGreaterThan(LocalDateTime yesterday);

    List<User> findByCreatedAtGreaterThanEqual(LocalDateTime yesterday);

    List<User> findByCreatedAtBetween(LocalDateTime yesterday, LocalDateTime tommorrow);

    List<User> findByIdBetween(Long id1, Long id2); // 양끝의 값을 포함 , findByIdGreaterThanEqualAndIdLessThanEqual 과 동일

    List<User> findByIdIsNotNull();

    // List<User> findByAddressIsNotEmpty();   // 많이 사용되지않는 메소드, name is not null and name != '' (x), sql exists문으로 조회

    List<User> findByNameIn(List<String> names);

    List<User> findByNameStartingWith(String names);
    List<User> findByNameEndingWith(String names);
    List<User> findByNameContains(String names);
    List<User> findByNameLike(String names);

    List<User> findTop1ByNameOrderByIdDesc(String name);    // findLast1ByName을 이렇게 사용
    List<User> findFirstByNameOrderByIdDescEmailAsc(String name);
    List<User> findFirstByName(String name, Sort sort);
    Page<User> findByName(String name, Pageable pageable);  // Page는 응답값, Pageable 요청값
    @Query(value = "select * from user limit 1;", nativeQuery = true)
    Map<String, Object> findRawRecord();
}

