package com.example.bookmanager2.service;

import com.example.bookmanager2.domain.Book;
import com.example.bookmanager2.repository.AuthorRepository;
import com.example.bookmanager2.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void transactionTest() {
        // 안티패턴 - try/catch로 막게되면 모든 테스트가 성공하게 된다.
        try {
            bookService.putBookAndAuthor();
            // bookService.put();  // 같은 클래스 내에서 트랜잭션과 private으로 선언된 메소드에 트랜잭션은 작동하지않는다.
        } catch (RuntimeException e) {
            System.out.println(">>> " + e.getMessage());
        }
        // checkedException - Exception, 롤백되지않는다. 반드시 개발자가 처리해줘야한다.
        // uncheckedException - RuntimeException, 롤백된다.
        // TransactionAspectSupport 클래스에 invokeWithinTransaction메소드에 completeTransactionAfterThrowing 메소드에서 rollbackOn메소드  구현
        // checkedException에 롤백을 하고싶다면 @Transcational의 속성중에 rollbackFor()를 설정해주면된다.

        System.out.println("books : " + bookRepository.findAll());
        System.out.println("authors : " + authorRepository.findAll());
    }

    @Test
    void isolationTest() {
        Book book = new Book();
        book.setName("JPA 강의");

        bookRepository.save(book);

        bookService.get(1L);

        System.out.println(">>> " + bookRepository.findAll());
    }
}