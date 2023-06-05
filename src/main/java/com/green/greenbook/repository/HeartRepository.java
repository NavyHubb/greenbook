package com.green.greenbook.repository;

import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Heart;
import com.green.greenbook.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByMemberAndArchive(Member member, Archive archive);
}