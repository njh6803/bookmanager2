package com.example.bookmanager2.domain;

import com.example.bookmanager2.domain.listener.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
// @EntityListeners(value = AuditingEntityListener.class)
public class UserHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;*/

    private String name;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // @OneToMany 참조하는 값은 One에 해당하는 PK아이디를 Many쪽에서 FK로 가지고 있게 된다. 즉 UserHistory에서는 userId 값을 가지게 된다는 의미이다.
    // 그래서 일반적인 상황에서는 @ManyToOne이 조금 더 깔끔하게 엔티티를 구성할 수 있다.
    // 해당 엔티티가 필요로하는 FK값을 엔티티가 함께 가지고있기 때문이다.
    @ManyToOne
    private User user;

    // Jpa에서는 연관된 객체를 FK를 이용해서 조회하는 것이 아니라 getter()를 통해서 가져오게 된다.
    // @OneToMany 을 사용해야할지 @ManyToOne를 사용해야할지 혹은 둘다 사용할지 결정하는 조건은 어느 엔티티에서 연관엔티티가 필요한지 생각해보면 된다.
    // UserHistory값을 조회해서 보면서 user 객체를 조회해볼 그런 케이스는 많이 없을 것이다.
    // 반대로 user라는 객체에서 변경 이력들을 보기 위해서 UserHistory를 조회하는 경우는 많이 생길 것이다.
    // 이런 경우에는 User에서 @OneToMany를 활용해서 UserHistory에 연관관계를 맺어주는것이 훨씬 나은 방법이다.
}
