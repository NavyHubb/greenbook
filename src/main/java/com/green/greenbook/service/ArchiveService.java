package com.green.greenbook.service;

import com.green.greenbook.domain.form.ArchiveForm;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ArchiveRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {

    private final ArchiveRepository archiveRepository;

    public Archive create(ArchiveForm form) {
        if (archiveRepository.findByTitle(form.getTitle()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_BOOKTITLE);
        }

        Archive archive = Archive.builder()
            .title(form.getTitle())
            .author(form.getAuthor())
            .publisher(form.getPublisher())
            .build();

        return archiveRepository.save(archive);
    }

    public Archive update(Long archiveId, ArchiveForm form) {
        Optional<Archive> optionalArchive = archiveRepository.findById(archiveId);
        if (!optionalArchive.isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ARCHIVE);
        }

        Archive archive = optionalArchive.get();
        archive.update(form);

        return archiveRepository.save(archive);
    }

    public String delete(Long archiveId) {
        Archive archive = archiveRepository.findById(archiveId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARCHIVE));

        archiveRepository.delete(archive);
        //TODO: 아카이브 삭제 시 이와 연관된 리뷰와 스크랩 함께 삭제 처리

        return "아카이브가 정상적으로 삭제되었습니다.";
    }

}