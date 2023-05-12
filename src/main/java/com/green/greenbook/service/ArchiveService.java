package com.green.greenbook.service;

import com.green.greenbook.domain.dto.ArchiveResponseDto;
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

    public ArchiveResponseDto create(ArchiveResponseDto responseDto) {
        if (archiveRepository.findByTitle(responseDto.getTitle()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_ISBN);
        }

        return archiveRepository.save(Archive.from(responseDto)).toServiceDto();
    }

    public Archive get(Long archiveId) {
        return getArchive(archiveId);
    }

    public ArchiveResponseDto update(Long archiveId, ArchiveResponseDto responseDto) {
        Archive archive = getArchive(archiveId);

        archive.update(responseDto);

        return archiveRepository.save(archive).toServiceDto();
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