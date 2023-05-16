package com.green.greenbook.service;

import com.green.greenbook.domain.model.Member;
import com.green.greenbook.domain.model.Review;
import com.green.greenbook.domain.model.Scrap;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.MemberRepository;
import com.green.greenbook.repository.ReviewRepository;
import com.green.greenbook.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public void create(Long memberId, Long reviewId) {
        Member member = getMember(memberId);
        Review review = getReview(reviewId);

        if (scrapRepository.findByMemberAndReview(member, review).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_SCRAP);
        }

        Scrap scrap = Scrap.builder()
                .member(member)
                .review(review)
                .build();

        scrapRepository.save(scrap);
        review.plusScrapCnt();  // TODO: Thread-safe 처리
        reviewRepository.save(review);
    }

    public void delete(Long memberId, Long reviewId) {
        Member member = getMember(memberId);
        Review review = getReview(reviewId);
        Scrap scrap = getScrap(member, review);

        if (!Objects.equals(scrap.getMember().getId(), memberId)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        scrapRepository.delete(scrap);
        review.minusScrapCnt();  // TODO: Thread-safe 처리
        reviewRepository.save(review);
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private Scrap getScrap(Member member, Review review) {
        return scrapRepository.findByMemberAndReview(member, review)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCRAP));
    }

}
