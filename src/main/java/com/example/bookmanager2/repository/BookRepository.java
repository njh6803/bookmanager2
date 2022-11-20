package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
