package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.Book;
import com.example.bookmanager2.domain.BookReviewInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReviewInfoRepositoryTest {
    @Autowired
    private BookReviewInfoRepository bookReviewInfoRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void crudTest() {
        BookReviewInfo bookReviewInfo = new BookReviewInfo();
        //bookReviewInfo.setBookId(1L);
        bookReviewInfo.setBook(givenBook());
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);

        bookReviewInfoRepository.save(bookReviewInfo);

        System.out.println(">>>> " + bookReviewInfoRepository.findAll());
    }

    @Test
    void crudTest2() {

        givenBookReviewInfo();

        Book result = bookReviewInfoRepository
                        .findById(1L)
                        .orElseThrow(RuntimeException::new)
                        .getBook();

        /*Book result = bookRepository.findById(
                bookReviewInfoRepository
                        .findById(1L)
                        .orElseThrow(RuntimeException::new)
                        .getBookId()
        ).orElseThrow(RuntimeException::new);*/

        System.out.println(">>> " + result);

        BookReviewInfo result2 = bookRepository
                .findById(1L)
                .orElseThrow(RuntimeException::new)
                .getBookReviewInfo();

        System.out.println(">>> " + result2);
    }

    private Book givenBook() {
        Book book = new Book();
        book.setName("JPA 초격자 패키지");
        book.setAuthorId(1L);
        //book.setPublisherId(1L);

        return bookRepository.save(book);

        // System.out.println(">>>> " + bookRepository.findAll());
    }

    private void givenBookReviewInfo() {
        BookReviewInfo bookReviewInfo = new BookReviewInfo();
        // bookReviewInfo.setBookId(1L);
        bookReviewInfo.setBook(givenBook());
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);

        bookReviewInfoRepository.save(bookReviewInfo);

        System.out.println(">>>> " + bookReviewInfoRepository.findAll());
    }
}