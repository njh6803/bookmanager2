package com.example.bookmanager2.repository;

import com.example.bookmanager2.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
