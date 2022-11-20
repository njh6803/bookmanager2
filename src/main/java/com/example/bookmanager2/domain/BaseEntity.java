package com.example.bookmanager2.domain;

import com.example.bookmanager2.domain.listener.Auditable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass // 해당 클래스의 필드를 상속받는 엔티티의 컬럼으로 포함시켜준다.
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity implements Auditable {
    @CreatedDate
    @Column(columnDefinition = "datatime(6) default now(6)", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "datatime(6) default now(6)", nullable = false)
    private LocalDateTime updatedAt;

    // @LastModifiedBy @CreatedBy
    // 생성 또는 수정한 사람의 정보를 함께 저장할 수 있는 기능.
    // 누가 언제 생성 또는 수정했는지 스프링 시큐리티를 활용해서 인증정보를 가지고 오게 되면 좀 더 편리하게 해당 기능을 사용할 수 있다.
}
