package com.example.bookmanager2.service;

import com.example.bookmanager2.domain.Author;
import com.example.bookmanager2.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    //@Transactional(propagation = Propagation.REQUIRED)
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional(propagation = Propagation.NESTED)    // 세이브포인트(중간저장)까지는 보장을 한다.  // JPA에서는 사용하지않는다
    public void putAuthor() {
        Author author = new Author();
        author.setName("노지형");

        authorRepository.save(author);

        throw new RuntimeException("오류가 발생하였습니다. transaction은 어떻게 될까요?");
    }
}
