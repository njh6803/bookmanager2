package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
