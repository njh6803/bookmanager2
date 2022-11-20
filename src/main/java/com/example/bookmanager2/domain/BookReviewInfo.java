package com.example.bookmanager2.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BookReviewInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // private Long bookId;

    private float averageReviewScore;

    private int reviewCount;

    @OneToOne(optional = false/*, mappedBy = "bookReviewInfo"*/)
    // default 값이 optional() 이므로 null이 존재할 수 있어 left outer join 으로 연결
    // optional = false - 반드시 존재한다. null을 허용하지않는다. inner join으로 연결
    // mappedBy - 소유를 하지않는 엔티티에 설정, 연관 키를 해당 테이블에서는 더이상 가지지않게 된다.(book_id가 안생김)
    private Book book;
}
