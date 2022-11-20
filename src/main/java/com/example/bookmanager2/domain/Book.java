package com.example.bookmanager2.domain;

import com.example.bookmanager2.domain.converter.BookStatusConverter;
import com.example.bookmanager2.domain.listener.Auditable;
import com.example.bookmanager2.repository.dto.BookStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate  // 변경된 데이터만 업데이트해준다.
/*@EntityListeners(value = MyEntityLitener.class)*/
// @EntityListeners(value = { AuditingEntityListener.class}) BaseEntity에 포함
@Where(clause = "deleted = false") // 항상 추가되는 조건
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private Long authorId;

    // private Long publisherId;

    @OneToOne(mappedBy = "book")
    @ToString.Exclude // 제외
    // stackoverflow 발생 이유 - 엔티티 릴레이션을 사용하는 경우에 특히 ToString() 같은 메소드가 순환참조가 걸리게 된다.
    // 특별히 필요한 경우가 아닌 이상 릴레이션을 단방향으로 걸거나 ToString에서 제외하는 처리가 필요하다.
    private BookReviewInfo bookReviewInfo;

    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    private Publisher publisher;

    // @ManyToMany
    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();
    //private List<Author> authors = new ArrayList<>();

    /*public void addAuthor(Author... author) {
        Collections.addAll(this.authors, author);
    }
    */

    private boolean deleted; // 삭제 여부

    // private int status; // 판매 상태
    @Convert(converter = BookStatusConverter.class)
    private BookStatus status; // 판매 상태

   /* public boolean isDisplayed() {
        return status == 200;
    }*/

    public void addBookAndAuthor(BookAndAuthor... bookAndAuthors) {
        Collections.addAll(this.bookAndAuthors, bookAndAuthors);
    }

    // User

    // user_product -> order

    // Product

/*    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;*/

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
