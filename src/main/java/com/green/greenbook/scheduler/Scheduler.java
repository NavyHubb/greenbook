package com.green.greenbook.scheduler;

import com.green.greenbook.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final ArchiveRepository archiveRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")  // 매일 자정
    public void deleteArchiveWithEmptyReviewList() {
        archiveRepository.deleteByReviewList_Empty();
        log.info("-----삭제 스케줄러 구동-----");
    }

}