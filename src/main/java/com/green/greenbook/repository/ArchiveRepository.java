package com.green.greenbook.repository;

import com.green.greenbook.domain.model.Archive;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional<Archive> findByIsbn(String isbn);

    void deleteByReviewList_Empty();
    Page<Archive> findAll(Pageable pageable);
}