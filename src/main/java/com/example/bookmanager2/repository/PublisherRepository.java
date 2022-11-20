package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
