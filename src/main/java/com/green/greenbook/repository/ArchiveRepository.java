package com.green.greenbook.repository;

import com.green.greenbook.domain.model.Archive;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional<Archive> findByIsbn(String isbn);
}