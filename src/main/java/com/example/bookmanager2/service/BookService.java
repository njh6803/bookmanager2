package com.example.bookmanager2.service;

import com.example.bookmanager2.domain.Author;
import com.example.bookmanager2.domain.Book;
import com.example.bookmanager2.repository.AuthorRepository;
import com.example.bookmanager2.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class BookService {
    // @Autowired // 최근에는 생성자 주입으로 한다.
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final EntityManager entityManager;
    private final AuthorService authorService;

    /*public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }*/

    /*public void put() {
        this.putBookAndAuthor();
    }*/

    @Transactional(/*rollbackFor = Exception.class,*/ propagation = Propagation.REQUIRED)
    void putBookAndAuthor() {
        Book book = new Book();
        book.setName("JPA 시작하기");

        bookRepository.save(book);
        try {
            authorService.putAuthor();
        } catch (RuntimeException e) {

        }

/*        Author author = new Author();
        author.setName("martin");

        authorRepository.save(author);

        throw new RuntimeException("오류가 나서 DB commit이 발생하지 않습니다.");*/

    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void get(Long id) {
        System.out.println(">>>> " + bookRepository.findById(id));
        System.out.println(">>>> " + bookRepository.findAll());

        entityManager.clear();

        System.out.println(">>>> " + bookRepository.findById(id));
        System.out.println(">>>> " + bookRepository.findAll());

        bookRepository.update();

        entityManager.clear();

        /*Book book = bookRepository.findById(id).get();
        book.setName("바뀔까");
        bookRepository.save(book);*/
    }
}
