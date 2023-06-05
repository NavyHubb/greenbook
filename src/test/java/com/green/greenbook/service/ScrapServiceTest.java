package com.green.greenbook.service;

import com.green.greenbook.domain.model.*;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.MemberRepository;
import com.green.greenbook.repository.ReviewRepository;
import com.green.greenbook.repository.ScrapRepository;
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
class ScrapServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ScrapRepository scrapRepository;
    @InjectMocks
    private ScrapService scrapService;

    @Test
    @DisplayName("스크랩 등록 실패 - 멤버 정보 찾을 수 없음")
    void create_fail_NOT_FOUND_MEMBER() {
        //given
        Member member = getMember();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> scrapService.create(1L, 1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
    }

    @Test
    @DisplayName("스크랩 등록 실패 - 리뷰 정보 찾을 수 없음")
    void create_fail_NOT_FOUND_REVIEW() {
        //given
        Member member = getMember();
        Review review = getReview();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(reviewRepository.findById(review.getId()))
                .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> scrapService.create(1L, 1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_REVIEW, exception.getErrorCode());
    }

    @Test
    @DisplayName("스크랩 등록 성공")
    void create_success() {
        //given
        Member member = getMember();
        Review review = getReview();
        long review_scrapCnt_before = review.getScrapCnt();
        Scrap scrap = Scrap.builder()
                .member(member)
                .review(review)
                .build();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(reviewRepository.findById(review.getId()))
                .willReturn(Optional.of(review));
        given(scrapRepository.save(any()))
                .willReturn(scrap);

        //when
        scrapService.create(member.getId(), review.getId());
        ArgumentCaptor<Scrap> captor = ArgumentCaptor.forClass(Scrap.class);

        //then
        verify(scrapRepository, times(1)).save(captor.capture());
        assertEquals(member.getId(), captor.getValue().getMember().getId());
        assertEquals(review.getId(), captor.getValue().getReview().getId());

        assertEquals(review_scrapCnt_before + 1, review.getScrapCnt());
    }

    @Test
    @DisplayName("스크랩 삭제 실패 - 스크랩 정보 찾을 수 없음")
    void delete_fail_NOT_FOUND_SCRAP() {
        //given
        Member member = getMember();
        Review review = getReview();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(reviewRepository.findById(review.getId()))
                .willReturn(Optional.of(review));
        given(scrapRepository.findByMemberAndReview(any(), any()))
                .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> scrapService.delete(1L, 1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_SCRAP, exception.getErrorCode());
    }

    @Test
    @DisplayName("스크랩 삭제 성공")
    void delete_success() {
        //given
        Member member = getMember();
        Review review = getReview();

        review.setScrapCnt(2);
        long review_scrapCnt_before = review.getScrapCnt();
        Scrap scrap = Scrap.builder()
                .member(member)
                .review(review)
                .build();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(reviewRepository.findById(review.getId()))
                .willReturn(Optional.of(review));
        given(scrapRepository.findByMemberAndReview(any(), any()))
                .willReturn(Optional.of(scrap));

        //when
        scrapService.delete(member.getId(), review.getId());
        ArgumentCaptor<Scrap> captor = ArgumentCaptor.forClass(Scrap.class);

        //then
        verify(scrapRepository, times(1)).delete(captor.capture());
        assertEquals(member.getId(), captor.getValue().getMember().getId());
        assertEquals(review.getId(), captor.getValue().getReview().getId());

        assertEquals(review_scrapCnt_before - 1, review.getScrapCnt());
    }

    private Member getMember() {
        Member member = new Member();
        member.setId(1L);

        return member;
    }

    private Review getReview() {
        Review review = new Review();
        review.setId(1L);

        return review;
    }
}