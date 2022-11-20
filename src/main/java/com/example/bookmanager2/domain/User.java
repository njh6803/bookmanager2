package com.example.bookmanager2.domain;

import com.example.bookmanager2.domain.listener.Auditable;
import com.example.bookmanager2.domain.listener.UserEntityListener;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor // 기본 생성자, JPA에서는 기본생성자가 항상 필요
@AllArgsConstructor // 객체가 가지고있는 모든 필드들을 인자로 받아서 생성해주는 생성자
@RequiredArgsConstructor // 꼭 필요한 인자만 받아서 생성, @NonNull 사용하면 필수 값이 됨
// @EqualsAndHashCode // JPA에서는 크게 사용할 일이 없음, 다만 @Data에서 구현하고있는 내용이고 또 자바에서 객체의 동등성을 비교하기 위해서 Equals() 와 HashCode()를 ToString()과 함께 오버라이딩해서 사용할 것을 권장
@Data // @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode 동일, oop 객체지향 원칙들을 깨트리는 몇가지 단점들을 내포하고있음
@Builder // AllArgsConstructor와 비슷하게 객체를 생성하고 필드값을 주입해주는데 빌더의 형식을 가지고 제공해준다.
@Entity // jpa에서 관리하는 엔티티임을 정의
// @Table(name = "user") // 일반적으로는 엔티티 이름에 맞는 테이블이름을 자동으로 지정해주기때문에 설정해줄 필요가 없다. 마이그레이션을 할 때 사용
@Table(indexes = {@Index(columnList = "name")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"name","email"})}) // @UniqueConstraint는 복합 컬럼 name과 email을 두개를 엮어서 Unique한 Constructor 만들 때 사용
/*
주의할것은 해당 인덱스나 제약사항은 실제로 DB에 적용된것과 다를 수 있다.
실제 DB에 인덱스 설정이 안되어 있는데 jpa 인덱스 설정이 있다고 해서 인덱스를 활용한 쿼리가 동작하거나 처리되지 않는다는 의미이다.
조금 보편적으로 봤을 때 인덱스나 제약사항들은 DB에 맡기고 엔티티에 표기하지않는 것이 좀 더 많은 것 같다
*/
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(value = { /*MyEntityLitener.class*/ /*AuditingEntityListener.class,*/ UserEntityListener.class}) // 스프링에서 제공하는 별도의 기본 리스너 사용
public class User extends BaseEntity {

    @Id // PK, 반드시 존재해야한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GenerationType - TABLE SEQUENCE IDENTITY AUTO
    // TABLE    - DB의 종류와 상관없이 아이디값을 관리하는 별도의 테이블을 만들어두고 그 테이블에서 아이디값을 계속해서 추출해서 사용할 수 있도록 제공하고 있다.
    // SEQUENCE - 오라클, postgresql, h2 등에서 사용, insert구문이 실행이 될 때 시퀀스로부터 증가된 값을 받아서 실제 트랜잭션이 종료되는 시점에 insert구문에 아이디를 채워서 쿼리를 하게된다.
    /* IDENTITY - mysql, mariaDb 에서 많이 사용하는 전략, 가장 흔한 설정값, 트랜잭션이 종료되기전에 insert문이 동작하게돼서 아이디값을 사전에 받아오게된다.
                  실제로 커밋되지않고 로직이 만약에 종료된다하더라고 DB에서 가지고있는 아이디값을 증가시키고 있어서 마치 이빨빠진것처럼 특정 아이디값이 비는 현상이 일어난다. */
    // AUTO     - 디폴트 값, 각 DB에 적합한 값을 자동으로 넘겨주게 된다. DB의존성 없이 코딩할 수 있다는 장점이 있다. 하지만 일반적으로 DB는 고정해서 사용하는 경우가 많기 때문에 구체적인 값을 지정해주고 사용해주는 경우가 많다.
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    /* 각 컬럼에 대한 설정, nullable - null 제약조건, unique - 유니크 여부,
    insertable,updatable - ddl이 아니라 일반적인 dml 쿼리에도 영향 */
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    // @Column(insertable = false)
    //@LastModifiedDate

    //private LocalDateTime updatedAt;

    //@Transient // 영속성 처리에서 제외가 된다. DB 데이터에 반영되지않고 해당 객체와 생명주기를 같이하게되는 그런 값이 된다.
    //private String testData;

    // enum은 자바에서 사용하는 일종의 상수 객체인데 엔티티에서는 별도의 처리방법을 가진다.
    @Enumerated(value = EnumType.STRING)   // 디폴트가 ORDINAL, enum의 순서가 MALE, FEMALE 이면 제로베이스 인덱스이기 때문에 MALE - 0, FEMALE - 1 이 나온다.
    // ORDINAL로 코드를 만드는 것은 잠재적인 버그를 일으키게 된다.
    // enum을 사용할 때에는 반드시 EnumType을 String으로 해줘야한다.
    private Gender gender;

    @OneToMany(fetch = FetchType.EAGER) // @JoinColumn이 없으면 user_user_histories 중간 관계테이블이 생성된다.
    // history 테이블은 readonly 이기때문에 insertable = false, updatable = false 설정해준다.
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // 엔티티가 어느 컬럼으로 조인할 것인지 지정해준다. 기본값으로 userHistories_id 가 생성되고 조인된다.
    // list 대신 복수형을 쓰는 것이 최근 트렌드이다. getUserHistory를 했을 때 NullPointException이 발생하지 않도록 기본적으로 기본리스트를 생성해준다.
    // 사실 Jpa에서는 해당 값을 조회할 때 값이 존재하지않으면 빈 리스트를 자동으로 넣어주게 된다. 그래서 일반적으로는 문제가 없지만 jpa에서 persist를 하기전에 해당값이 Null이기 때문에 로직에 따라서는 NullPointException이 발생할 수도 있다.
    @ToString.Exclude
    private List<UserHistory> userHistories = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();


    /*@OneToMany(fetch = FetchType.EAGER)
    private List<Address> address;*/

    /*
        Entity Listener
        리스너라는 것은 일종의 이벤트를 관찰하고 있다가 이벤트가 발생하게 되면 특정 동작을 진행한다.
        Entity 리스너이기때문에 엔티티가 동작하는 몇가지 방법에 대한 이벤트를 리스닝하고 있다.
    */
    // JPA에서 제공하고 있는 리스너는 7가지가 있다.
/*    @PrePersist // insert 메소드가 호출 되기 전 실행되는 메소드
    public void perPersist() {
        System.out.println(">>> prePersist");
    }
    @PreUpdate // merge 메소드가 호출 되기 전 실행되는 메소드
    public void preUpdate() {
        System.out.println(">>> preUpdate");
    }
    @PreRemove
    public void preRemove() { // delete 메소드가 호출 되기 전 실행되는 메소드
        System.out.println(">>> preRemove");
    }
    @PostPersist // insert 메소드가 호출 된 후 실행되는 메소드
    public void postPersist() {
        System.out.println(">>> postPersist");
    }
    @PostUpdate // merge 메소드가 호출 된 후 실행되는 메소드
    public void postUpdate() {
        System.out.println(">>> postUpdate");
    }
    @PostRemove // delete 메소드가 호출 된 후 실행되는 메소드
    public void postRemove() {
        System.out.println(">>> postRemove");
    }
    @PostLoad   // select 조회가 일어난 직 후에 실행되는 메소드
    public void postLoad() {
        System.out.println(">>> postLoad");
    }*/

/*    @PrePersist
    public void perPersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }*/

}
