package com.green.greenbook.service;

import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Heart;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ArchiveRepository;
import com.green.greenbook.repository.HeartRepository;
import com.green.greenbook.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HeartServiceTest {

    @Mock
    private HeartRepository heartRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ArchiveRepository archiveRepository;

    @InjectMocks
    private HeartService heartService;


    @Test
    @DisplayName("하트 등록 실패 - 아카이브 정보 찾을 수 없음")
    void create_fail_NOT_FOUND_ARCHIVE() {
        //given
        Member member = getMember();
        Archive archive = getArchive();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(archiveRepository.findById(archive.getId()))
                .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> heartService.create(1L, 1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_ARCHIVE, exception.getErrorCode());
    }

    @Test
    @DisplayName("하트 등록 성공")
    void create_success() {
        //given
        Member member = getMember();
        Archive archive = getArchive();
        long archive_heartCnt_before = archive.getHeartCnt();
        Heart heart = Heart.builder()
                .member(member)
                .archive(archive)
                .build();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(archiveRepository.findById(archive.getId()))
                .willReturn(Optional.of(archive));
        given(heartRepository.save(any()))
                .willReturn(heart);

        //when
        heartService.create(member.getId(), archive.getId());
        ArgumentCaptor<Heart> captor = ArgumentCaptor.forClass(Heart.class);

        //then
        verify(heartRepository, times(1)).save(captor.capture());
        assertEquals(member.getId(), captor.getValue().getMember().getId());
        assertEquals(archive.getId(), captor.getValue().getArchive().getId());
        assertEquals(archive_heartCnt_before + 1, archive.getHeartCnt());
    }

    @Test
    @DisplayName("하트 삭제 실패 - 아카이브 정보 찾을 수 없음")
    void delete_fail_NOT_FOUND_ARCHIVE() {
        //given
        Member member = getMember();
        Archive archive = getArchive();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(archiveRepository.findById(archive.getId()))
                .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> heartService.delete(1L, 1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_ARCHIVE, exception.getErrorCode());
    }

    @Test
    @DisplayName("하트 삭제 성공")
    void delete_success() {
        //given
        Member member = getMember();
        Archive archive = getArchive();
        archive.setHeartCnt(2);
        long archive_heartCnt_before = archive.getHeartCnt();
        Heart heart = Heart.builder()
                .member(member)
                .archive(archive)
                .build();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(archiveRepository.findById(archive.getId()))
                .willReturn(Optional.of(archive));
        given(heartRepository.findByMemberAndArchive(any(), any()))
                .willReturn(Optional.of(heart));

        //when
        heartService.delete(member.getId(), archive.getId());
        ArgumentCaptor<Heart> captor = ArgumentCaptor.forClass(Heart.class);

        //then
        verify(heartRepository, times(1)).delete(captor.capture());
        assertEquals(member.getId(), captor.getValue().getMember().getId());
        assertEquals(archive.getId(), captor.getValue().getArchive().getId());
        assertEquals(archive_heartCnt_before - 1, archive.getHeartCnt());
    }


    private Member getMember() {
        Member member = new Member();
        member.setId(1L);

        return member;
    }

    private Archive getArchive() {
        Archive archive = new Archive();
        archive.setId(1L);

        return archive;
    }
}