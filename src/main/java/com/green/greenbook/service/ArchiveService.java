package com.green.greenbook.service;

import com.green.greenbook.domain.form.ArchiveForm;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ArchiveRepository;
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

        return archiveRepository.save(Archive.from(form));
    }

    public Archive get(Long archiveId) {
        return getArchive(archiveId);
    }

    public Archive update(Long archiveId, ArchiveForm form) {
        Archive archive = getArchive(archiveId);

        archive.update(form);

        return archiveRepository.save(archive);
    }

    private Archive getArchive(Long archiveId) {
        Archive archive = archiveRepository.findById(archiveId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARCHIVE));
        return archive;
    }

    public String delete(Long archiveId) {
        Archive archive = getArchive(archiveId);

        archiveRepository.delete(archive);
        //TODO: 아카이브 삭제 시 이와 연관된 리뷰와 스크랩 함께 삭제 처리

        return "아카이브가 정상적으로 삭제되었습니다.";
    }

}