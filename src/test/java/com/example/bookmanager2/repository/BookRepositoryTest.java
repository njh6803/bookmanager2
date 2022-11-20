package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.Book;
import com.example.bookmanager2.domain.Publisher;
import com.example.bookmanager2.domain.Review;
import com.example.bookmanager2.domain.User;
import com.example.bookmanager2.repository.dto.BookStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository  userRepository;

    @Test
    void bookTest() {
/*        Book book = new Book();
        book.setName("JPA 초격자 패키지");
        book.setAuthor("패스트캠퍼스");

        bookRepository.save(book);

        System.out.println(bookRepository.findAll());*/

    }

    @Test
    void bookTest2() {
        Book book = new Book();
        book.setName("JPA 초격자 패키지");
        book.setAuthorId(1L);
        // book.setPublisherId(1L);

        bookRepository.save(book);

        System.out.println(bookRepository.findAll());

    }

    @Test
    @Transactional
    void bookRelationTest() {
        givenBookAndReview();

        User user = userRepository.findByEmail("martin@fastcampus.com");

        System.out.println("Review : " + user.getReviews());
        System.out.println("Book : " + user.getReviews().get(0).getBook());
        System.out.println("Publisher : " + user.getReviews().get(0).getBook().getPublisher());
    }

    @Test
    void bookCascadeTest() {
        Book book = new Book();
        book.setName("JPA 초격차 패키지");

        // bookRepository.save(book);

        Publisher publisher = new Publisher();
        publisher.setName("패스트캠퍼스");
        // publisherRepository.save(publisher);

        book.setPublisher(publisher);
        bookRepository.save(book);

        // publisher.getBooks().add(book);
        /*publisher.addBook(book);
        publisherRepository.save(publisher);*/

        // object references an unsaved transient instance - save the transient instance before flushing - 영속성 전이가 안됐다.

        System.out.println("books : " + bookRepository.findAll());
        System.out.println("pubilshers : " + publisherRepository.findAll());

        // could not initialize proxy - no Session - 트랜잭션이 존재하지 않는다? @Transactional붙여주거나 @ToString.Exclude 순환참조 제거

        Book book1 = bookRepository.findById(1L).get(); //  CascadeType.PERSIST merge에 대한 Cascade를 하지않았기 때문에 값이 변경되지않는다.
        book1.getPublisher().setName("슬로우캠퍼스");

        bookRepository.save(book1);

        System.out.println("pubilshers : " + publisherRepository.findAll());

        Book book2 = bookRepository.findById(1L).get();
        // bookRepository.delete(book2);
        // bookRepository.deleteById(1L);

        // publisherRepository.delete(book2.getPublisher());

        Book book3 = bookRepository.findById(1L).get();
        book3.setPublisher(null);

        bookRepository.save(book3);

        System.out.println("books : " + bookRepository.findAll());
        System.out.println("pubilshers : " + publisherRepository.findAll());
        System.out.println("book3-publisher : " + bookRepository.findById(1L).get().getPublisher());

    }

    @Test
    void bookRemoveCasCadeTest() {
        bookRepository.deleteById(1L);

        System.out.println("books : " + bookRepository.findAll());
        System.out.println("pubilshers : " + publisherRepository.findAll());

        bookRepository.findAll().forEach(book -> System.out.println(book.getPublisher()));
    }

    @Test
    void softDelete() {
        bookRepository.findAll().forEach(System.out::println);
        System.out.println(bookRepository.findById(3L));

//        bookRepository.findByCategoryIsNull().forEach(System.out::println);
//        bookRepository.findAllByDeletedFalse().forEach(System.out::println);
//        bookRepository.findByCategoryIsNullAndDeletedFalse().forEach(System.out::println);
    }

    @Test
    void ConverterTest() {
        bookRepository.findAll().forEach(System.out::println);

        Book book = new Book();
        book.setName("또다른 IT전문서적");
        book.setStatus(new BookStatus(200));

        bookRepository.save(book);
    }

    private void givenBookAndReview() {
        givenReview(givenUser(), givenBook(givenPublisher()));
    }

    private User givenUser() {
        return userRepository.findByEmail("martin@fastcampus.com");
    }

    private void givenReview(User user, Book book) {
        Review review = new Review();
        review.setTitle("내 인생을 바꾼 책");
        review.setContent("너무 너무 재미있고 즐거운 책이었어요");
        review.setScore(5.0f);
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);
    }

    private Book givenBook(Publisher publisher) {
        Book book = new Book();
        book.setName("JPA 초격자 패키지");
        book.setPublisher(publisher);

        return bookRepository.save(book);
    }

    private Publisher givenPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("패스트캠퍼스");

        return publisherRepository.save(publisher);
    }
}
