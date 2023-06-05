package com.green.greenbook.service;

import com.green.greenbook.domain.dto.ReviewDto;
import com.green.greenbook.domain.dto.ReviewRequest;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.domain.model.Review;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ArchiveRepository;
import com.green.greenbook.repository.MemberRepository;
import com.green.greenbook.repository.ReviewRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ArchiveRepository archiveRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("리뷰 생성 - 실패 - 같은 제목으로 이미 등록된 리뷰가 존재함")
    void create_fail_ALREADY_REGISTERED_HEAD() {
        //given
        Member member = getMember();
        Archive archive = getArchive();
        Review review = getReview();

        given(archiveRepository.findById(anyLong()))
                .willReturn(Optional.of(archive));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(reviewRepository.findByMemberAndHead(any(), any()))
                .willReturn(Optional.of(review));

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.create(archive.getId(), member.getId(), "head", "content"));

        //then
        assertEquals(ErrorCode.ALREADY_REGISTERED_HEAD, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 생성 - 성공")
    void create_success() {
        //given
        Member member = getMember();
        Archive archive = getArchive();
        Review review = getReview();
        ReviewRequest request = getReviewRequest();

        given(archiveRepository.findById(anyLong()))
                .willReturn(Optional.of(archive));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(reviewRepository.save(any()))
                .willReturn(review);

        //when
        reviewService.create(archive.getId(), member.getId(), request.getHead(), request.getContent());
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        //then
        verify(reviewRepository, times(1)).save(captor.capture());
        assertEquals(request.getHead(), captor.getValue().getHead());
        assertEquals(request.getContent(), captor.getValue().getContent());
    }

    @Test
    @DisplayName("리뷰 조회 - 실패 - 아이디 값에 해당하는 리뷰 없음")
    void get_fail() {
        //given
        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.get(1L));

        //then
        assertEquals(ErrorCode.NOT_FOUND_REVIEW, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 조회 - 성공")
    void get_success() {
        //given
        Review review = getReview();

        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        //when
        ReviewDto dto = reviewService.get(review.getId());

        //then
        assertEquals(review.getMember().getNickname(), dto.getNickname());
        assertEquals(review.getHead(), dto.getHead());
        assertEquals(review.getContent(), dto.getContent());
        assertEquals(review.getScrapCnt(), dto.getScrapCnt());
    }

    @Test
    @DisplayName("리뷰 수정 - 실패 - 해당 리뷰 없음")
    void update_fail_NOT_FOUND_REVIEW() {
        //given
        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.update(1L, 1L, "head", "content"));

        //then
        assertEquals(ErrorCode.NOT_FOUND_REVIEW, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 수정 - 실패 - 수정 권한 없음")
    void update_fail_NO_AUTHORIZATION() {
        //given
        Member member = getMember();
        member.setId(2L);
        Review review = getReview();

        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.update(1L, member.getId(), "head", "content"));

        //then
        assertEquals(ErrorCode.NO_AUTHORIZATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 수정 - 성공")
    void update_success() {
        //given
        String newHead = "newHead";
        String newContent = "newContent";

        Review review = getReview();
        Member member = getMember();

        Review updatedReview = getReview();
        updatedReview.setHead(newHead);
        updatedReview.setHead(newContent);

        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));
        given(reviewRepository.save(any()))
                .willReturn(updatedReview);

        //when
        reviewService.update(review.getId(), member.getId(), newHead, newContent);
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        //then
        verify(reviewRepository, times(1)).save(captor.capture());
        assertEquals(newHead, captor.getValue().getHead());
        assertEquals(newContent, captor.getValue().getContent());
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패 - 수정 권한 없음")
    void delete_fail_NO_AUTHORIZATION() {
        //given
        Member member = getMember();
        member.setId(2L);
        Review review = getReview();

        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.delete(1L, member.getId()));

        //then
        assertEquals(ErrorCode.NO_AUTHORIZATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 삭제 - 성공")
    void delete_success() {
        //given
        Review review = getReview();

        given(reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        //when
        reviewService.delete(review.getId(), 1L);

        //then
        verify(reviewRepository, times(1)).delete(review);
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

    private Review getReview() {
        Member member = getMember();
        ReviewRequest request = getReviewRequest();

        return Review.builder()
                .id(1L)
                .member(member)
                .head(request.getHead())
                .content(request.getContent())
                .scrapCnt(0)
                .build();
    }

    private ReviewRequest getReviewRequest() {
        return ReviewRequest.builder()
                .head("head")
                .content("content")
                .build();
    }
}