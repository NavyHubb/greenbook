package com.green.greenbook.repository;

import com.green.greenbook.domain.model.Member;
import com.green.greenbook.domain.model.Review;
import com.green.greenbook.domain.model.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByMemberAndReview(Member member, Review review);
}
