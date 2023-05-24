package com.green.greenbook.service;

import com.green.greenbook.domain.dto.ReviewDto;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.domain.model.Review;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.property.ReviewProperty;
import com.green.greenbook.repository.ArchiveRepository;
import com.green.greenbook.repository.MemberRepository;
import com.green.greenbook.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ArchiveRepository archiveRepository;
    private final MemberRepository memberRepository;

    public void create(Long archiveId, Long memberId, String head, String content) {
        Archive archive = getArchive(archiveId);
        Member member = getMember(memberId);

        if (reviewRepository.findByMemberAndHead(member, head).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_HEAD);
        }

        Review review = Review.builder()
                .member(member)
                .archive(archive)
                .head(head)
                .content(content)
                .build();

        reviewRepository.save(review);
    }

    public ReviewDto get(Long reviewId) {
        return getReview(reviewId).toDto();
    }

    public ReviewDto update(Long reviewId, Long memberId, String head, String content) {
        Review review = getReview(reviewId);

        if (!Objects.equals(review.getMember().getId(), memberId)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        review.update(head, content);

        return reviewRepository.save(review).toDto();
    }

    public void delete(Long reviewId, Long memberId) {
        Review review = getReview(reviewId);

        if (!Objects.equals(review.getMember().getId(), memberId)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        reviewRepository.delete(review);
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
    }

    private Archive getArchive(Long archiveId) {
        return archiveRepository.findById(archiveId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARCHIVE));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

}