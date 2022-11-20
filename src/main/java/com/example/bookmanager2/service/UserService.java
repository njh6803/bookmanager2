package com.example.bookmanager2.service;

import com.example.bookmanager2.domain.User;
import com.example.bookmanager2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
public class UserService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void put() {
        User user = new User();
        user.setName("newUser");
        user.setEmail("newUser@fastcampus.com");

        // userRepository.save(user); // save() 구현체 안에 엔티티매니저 객체가 영속화 시키고 있기때문에 영속화 된다.

        entityManager.persist(user); // 직접 영속화, 영속성 컨텍스트가 해당 엔티티를 관리하는 상태, 즉 managed 상태
        // entityManager.detach(user); // 준영속 상태. 영속성 컨텍스트에서 더이상 관리하지않는다.

        user.setName("newUserAfterPersist");
        entityManager.merge(user); // 준영속 상태라도 머지를 해주면 데이터반영이 된다.
        // entityManager.flush();
        // entityManager.clear(); // 준영속 상태. clear()를 하기전에 반드시 flush() 메소드를 호출해서 변경내용을 모두 반영하기를 권고한다.

        User user1 = userRepository.findById(1L).get();
        entityManager.remove(user1); // detach 상태에서는 안됨
        // 영속성 컨텍스트내어서 관리되는 엔티티는 setter를 통해서 엔티티 객체의 정보가 변경된 경우에 @Transactional 완료되는 시점에 별도로
        // save() 메소드를 호출하지않아도 DB 데이터의 정합성을 맞춰준다.

        // save()를 하지 않았지만 update쿼리가 실행된 부분은 영속성 컨텍스트가 제공하는 더티체크라는 기능이다.
        // 더티체크 - 말 그대로 더러운지 감지한다. 더티라는게 변경이 됐다라는 그런 의미.
        //           그래서 영속성 컨텍스트가 가지고있는 엔티티 객체에는 처음 컨텍스트에 로드를 할 때 해당 정보를 일종의 스냅샷으로 복사를 해서 가지고 있는다.
        //           그런 후에 변경 내용을 DB에 적용해야하는 시점에 DB에 적용하려고 할 때 해당 스냅샷과 현재 엔티티의 값을 일일히 비교해서 변경된 내용이 있다면
        //           추가적으로 save코드가 없다하더라도 DB에 변경을 반영해준다. 대량의 엔티티를 다루게 될 경우에는 로직에 성능적인 저하가 발생하기도 한다.

        user1.setName("marrrrrrrrrrrtin");
        entityManager.merge(user1);

    }
}
