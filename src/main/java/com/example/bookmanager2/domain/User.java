package com.example.bookmanager2.domain;

import jdk.jfr.Enabled;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor // 기본 생성자, JPA에서는 기본생성자가 항상 필요
@AllArgsConstructor // 객체가 가지고있는 모든 필드들을 인자로 받아서 생성해주는 생성자
@RequiredArgsConstructor // 꼭 필요한 인자만 받아서 생성, @NonNull 사용하면 필수 값이 됨
@EqualsAndHashCode // JPA에서는 크게 사용할 일이 없음, 다만 @Data에서 구현하고있는 내용이고 또 자바에서 객체의 동등성을 비교하기 위해서 Equals() 와 HashCode()를 ToString()과 함께 오버라이딩해서 사용할 것을 권장
@Data // @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode 동일, oop 객체지향 원칙들을 깨트리는 몇가지 단점들을 내포하고있음
@Builder // AllArgsConstructor와 비슷하게 객체를 생성하고 필드값을 주입해주는데 빌더의 형식을 가지고 제공해준다.
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
