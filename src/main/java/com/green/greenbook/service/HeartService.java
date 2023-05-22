package com.green.greenbook.service;

import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Heart;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.property.ArchiveProperty;
import com.green.greenbook.repository.ArchiveRepository;
import com.green.greenbook.repository.HeartRepository;
import com.green.greenbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ArchiveProperty archiveProperty;
    private final RedissonService redissonService;

    public void create(Long memberId, Long archiveId) {
        Member member = getMember(memberId);
        Archive archive = getArchive(archiveId);

        if (heartRepository.findByMemberAndArchive(member, archive).isPresent()){
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_HEART);
        }

        heartRepository.save(Heart.builder()
                                    .member(member)
                                    .archive(archive)
                                    .build());

        String key = redissonService.keyResolver(archiveProperty, archive.getIsbn());
        redissonService.updateValue(key, true);

        archive.setHeartCnt(redissonService.getValue(key));
    }

    public void delete(Long memberId, Long archiveId) {
        Member member = getMember(memberId);
        Archive archive = getArchive(archiveId);
        Heart heart = getHeart(member, archive);

        heartRepository.delete(heart);

        String key = redissonService.keyResolver(archiveProperty, archive.getIsbn());
        redissonService.updateValue(key, false);

        archive.setHeartCnt(redissonService.getValue(key));
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