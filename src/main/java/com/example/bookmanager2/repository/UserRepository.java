package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
// JpaRepository - PagingAndSortingRepository와 QueryByExampleExecutor를 상속받는다.
// QueryByExampleExecutor - Example객체를 활용한 메소드들을 제공
// PagingAndSortingRepository - CrudRepository를 상속받는다. Pageable를 이용하여 페이징을 도와주는 기능을 내장하고있다.
// CrudRepository - 우리가 사용하는 대부분의 기본 메소드들은 여기에 정의되어있다.
public interface UserRepository extends JpaRepository<User/* Entity */, Long /* Id 값*/> {
}

// data.sql 이라는 파일을 resources폴더 하위에 두면 JPA가 로딩을 할 때 자동으로 해당 쿼리를 한번 실행한다