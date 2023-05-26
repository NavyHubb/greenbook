package com.green.greenbook.service;

import com.green.greenbook.domain.dto.ArchiveCreateRequest;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.property.ArchiveProperty;
import com.green.greenbook.repository.ArchiveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArchiveServiceTest {

    @Mock
    private ArchiveRepository archiveRepository;
    @Mock
    private RedissonService redissonService;
    @InjectMocks
    private ArchiveService archiveService;

    @Test
    @DisplayName("중복된 ISBN으로 아카이브 생성 시도 시 실패")
    void create_fail_ALREADY_REGISTERED_ISBN() {
        //given
        ArchiveCreateRequest request = getRequest();
        given(archiveRepository.findByIsbn(anyString()))
            .willReturn(Optional.of(getArchive()));

        //when
        CustomException exception = assertThrows(CustomException.class,
            () -> archiveService.create(
                    request.getIsbn(), request.getTitle(), request.getAuthor(), request.getPublisher()
            ));

        //then
        assertEquals(ErrorCode.ALREADY_REGISTERED_ISBN, exception.getErrorCode());
    }
    
    @Test
    @DisplayName("아카이브 생성 성공")
    void create_success() {
        //given
        ArchiveCreateRequest request = getRequest();
        given(archiveRepository.save(any()))
            .willReturn(Archive.builder()
                .build());
        
        //when
        archiveService.create(request.getIsbn(), request.getTitle(), request.getAuthor(), request.getPublisher());
        ArgumentCaptor<Archive> captor = ArgumentCaptor.forClass(Archive.class);
        
        //then
        verify(archiveRepository, times(1)).save(captor.capture());
        assertEquals(request.getIsbn(), captor.getValue().getIsbn());
        assertEquals(request.getTitle(), captor.getValue().getTitle());
        assertEquals(request.getAuthor(), captor.getValue().getAuthor());
        assertEquals(request.getPublisher(), captor.getValue().getPublisher());
    }

    @Test
    @DisplayName("조회_실패_해당 아카이브 없음")
    void get_fail_NOT_FOUND_ARCHIVE() {
        //given
        given(archiveRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
            () -> archiveService.get(1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_ARCHIVE, exception.getErrorCode());
    }

    @Test
    @DisplayName("조회_성공")
    void get_success() {
        //given
        given(archiveRepository.findById(anyLong()))
            .willReturn(Optional.of(getArchive()));

        //when
        archiveService.get(1L);
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        //then
        verify(archiveRepository, times(1)).findById(captor.capture());
        assertEquals(1L, captor.getValue());
    }

    @Test
    @DisplayName("삭제_실패_해당 아카이브 없음")
    void delete_fail_NOT_FOUND_ARCHIVE() {
        //given
        given(archiveRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
            () -> archiveService.delete(1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_ARCHIVE, exception.getErrorCode());
    }

    @Test
    @DisplayName("삭제_성공")
    void delete_success() {
        //given
        given(archiveRepository.findById(anyLong()))
            .willReturn(Optional.of(getArchive()));

        //when
        archiveService.delete(1L);
        ArgumentCaptor<Archive> captor = ArgumentCaptor.forClass(Archive.class);

        //then
        verify(archiveRepository, times(1)).delete(captor.capture());

        // TODO: @SQLDelete 사용으로 정의한 deletedAt 값이 들어가는 부분 확인할 필요 있음
    }

    private Archive getArchive() {
        ArchiveCreateRequest request = getRequest();
        Archive archive = Archive.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .heartCnt(0)
                .build();
        archive.setId(1L);

        return archive;
    }

    private ArchiveCreateRequest getRequest() {
        return ArchiveCreateRequest.builder()
            .isbn("123456789")
            .title("Demian")
            .author("Charles")
            .publisher("Phony")
            .build();
    }

}