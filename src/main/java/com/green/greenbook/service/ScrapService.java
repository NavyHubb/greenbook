package com.green.greenbook.service;

import com.green.greenbook.domain.model.Member;
import com.green.greenbook.domain.model.Review;
import com.green.greenbook.domain.model.Scrap;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.MemberRepository;
import com.green.greenbook.repository.ReviewRepository;
import com.green.greenbook.repository.ScrapRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    private final RedissonClient redissonClient;

    public void createWithLock(Long memberId, Long reviewId) {
        final String lockName = "ScrapCreateLock: " + reviewId;
        final RLock lock = redissonClient.getLock(lockName);

        try {
            if(!lock.tryLock(1, 3, TimeUnit.SECONDS))
                return;

            create(memberId, reviewId);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.TRANSACTION_LOCK);
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    public void create(Long memberId, Long reviewId) {
        Member member = getMember(memberId);
        Review review = getReview(reviewId);

        if (scrapRepository.findByMemberAndReview(member, review).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_SCRAP);
        }

        scrapRepository.save(Scrap.builder()
            .member(member)
            .review(review)
            .build());

        review.setScrapCnt(review.getScrapCnt() + 1);
        reviewRepository.saveAndFlush(review);
    }

    public void deleteWithLock(Long memberId, Long reviewId) {
        final String lockName = "ScrapDeleteLock: " + reviewId;
        final RLock lock = redissonClient.getLock(lockName);

        try {
            if(!lock.tryLock(1, 3, TimeUnit.SECONDS))
                return;

            delete(memberId, reviewId);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.TRANSACTION_LOCK);
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    public void delete(Long memberId, Long reviewId) {
        Member member = getMember(memberId);
        Review review = getReview(reviewId);
        Scrap scrap = getScrap(member, review);

        scrapRepository.delete(scrap);

        review.setScrapCnt(review.getScrapCnt() - 1);
        reviewRepository.saveAndFlush(review);
    }

    private Scrap getScrap(Member member, Review review) {
        return scrapRepository.findByMemberAndReview(member, review)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCRAP));
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

}
