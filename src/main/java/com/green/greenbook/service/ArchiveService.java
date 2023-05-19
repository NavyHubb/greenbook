package com.green.greenbook.service;

import com.green.greenbook.domain.dto.ArchiveResponse;
import com.green.greenbook.domain.dto.ArchiveDto;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.property.ArchiveProperty;
import com.green.greenbook.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final ArchiveProperty archiveProperty;
    private final RedissonService redissonService;

    public ArchiveDto create(String isbn, String title, String author, String publisher) {
        if (archiveRepository.findByIsbn(isbn).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_ISBN);
        }

        Archive archive = createArchive(isbn, title, author, publisher);

        String key = redissonService.keyResolver(archiveProperty, archive.getIsbn());
        redissonService.setHeartCnt(key, 0);

        return archiveRepository.save(archive).toDto();
    }

    private Archive createArchive(String isbn, String title, String author, String publisher) {
        return Archive.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .publisher(publisher)
                .heartCnt(0)
                .build();
    }

    public Page<ArchiveDto> getAll(Pageable pageable) {
        return toDtoList(archiveRepository.findAll(pageable));
    }

    public Page<ArchiveDto> toDtoList(Page<Archive> archiveList){
        return archiveList.map(m -> ArchiveDto.builder()
            .isbn(m.getIsbn())
            .title(m.getTitle())
            .author(m.getAuthor())
            .publisher(m.getPublisher())
            .heartCnt(m.getHeartCnt())
            .build());
    }

    public Archive get(Long archiveId) {
        return getArchive(archiveId);
    }

    public ArchiveResponse update(Long archiveId, String title, String author, String publisher) {
        Archive archive = getArchive(archiveId);

        archive.update(title, author, publisher);

        return archiveRepository.save(archive).toResponse();
    }

    private Archive getArchive(Long archiveId) {
        return archiveRepository.findById(archiveId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARCHIVE));
    }

    public String delete(Long archiveId) {
        Archive archive = getArchive(archiveId);

        archiveRepository.delete(archive);
        //TODO: 아카이브 삭제 시 이와 연관된 리뷰와 스크랩 함께 삭제 처리

        return "아카이브가 정상적으로 삭제되었습니다.";
    }

}