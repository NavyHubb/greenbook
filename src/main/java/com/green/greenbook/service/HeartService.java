package com.green.greenbook.service;

import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Heart;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ArchiveRepository;
import com.green.greenbook.repository.HeartRepository;
import com.green.greenbook.repository.MemberRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final ArchiveRepository archiveRepository;

    private final RedissonClient redissonClient;

    public void create(Long memberId, Long archiveId) {
        final String lockName = "HeartCreateLock: " + archiveId;
        final RLock lock = redissonClient.getLock(lockName);

        try {
            if(!lock.tryLock(10, 20, TimeUnit.SECONDS))
                return;

            Member member = getMember(memberId);
            Archive archive = getArchive(archiveId);

            if (heartRepository.findByMemberAndArchive(member, archive).isPresent()){
                throw new CustomException(ErrorCode.ALREADY_REGISTERED_HEART);
            }

            heartRepository.save(Heart.builder()
                .member(member)
                .archive(archive)
                .build());

            archive.setHeartCnt(archive.getHeartCnt() + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.TRANSACTION_LOCK);
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    public void delete(Long memberId, Long archiveId) {
        final String lockName = "HeartDeleteLock: " + archiveId;
        final RLock lock = redissonClient.getLock(lockName);

        try {
            if(!lock.tryLock(1, 3, TimeUnit.SECONDS))
                return;

            Member member = getMember(memberId);
            Archive archive = getArchive(archiveId);
            Heart heart = getHeart(member, archive);

            heartRepository.delete(heart);

            archive.setHeartCnt(archive.getHeartCnt() - 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.TRANSACTION_LOCK);
        } finally {
            if(lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    private Heart getHeart(Member member, Archive archive) {
        return heartRepository.findByMemberAndArchive(member, archive)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HEART));
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