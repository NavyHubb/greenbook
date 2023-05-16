package com.green.greenbook.service;

import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Heart;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import com.green.greenbook.repository.ArchiveRepository;
import com.green.greenbook.repository.HeartRepository;
import com.green.greenbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final ArchiveRepository archiveRepository;

    public void create(Long memberId, Long archiveId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Archive archive = archiveRepository.findById(archiveId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARCHIVE));

        if (heartRepository.findByMemberAndArchive(member, archive).isPresent()){
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_HEART);
        }

        heartRepository.save(Heart.builder()
                                    .member(member)
                                    .archive(archive)
                                    .build());
        archive.plusHeart();  // TODO: Thread-safe 처리
        archiveRepository.save(archive);
    }

    public void delete(Long memberId, Long archiveId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Archive archive = archiveRepository.findById(archiveId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ARCHIVE));

        Heart heart = heartRepository.findByMemberAndArchive(member, archive)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HEART));

        heartRepository.delete(heart);
        archive.minusHeart();  // TODO: Thread-safe 처리
        archiveRepository.save(archive);
    }

}