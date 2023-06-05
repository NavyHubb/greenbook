package com.green.greenbook.repository;

import com.green.greenbook.domain.model.Member;
import com.green.greenbook.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByMemberAndHead(Member member, String head);
}