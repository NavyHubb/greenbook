package com.green.greenbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.green.greenbook.domain.form.SignUpForm;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.repository.ArchiveRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class ConcurrencyHeartServiceTest {

    @Autowired
    private HeartService heartService;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private ArchiveRepository archiveRepository;

    ExecutorService executorService;
    CountDownLatch countDownLatch;

    @BeforeEach
    void beforeEach() {
        executorService = Executors.newFixedThreadPool(20);
        countDownLatch = new CountDownLatch(20);
    }

    @Test
    @DisplayName("하트 생성 성공, 20명이 동시에 하트 등록")
    void create_sucess() throws InterruptedException {
        //given
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            SignUpForm form = SignUpForm.builder()
                .email(i+"@naver.com")
                .password("password11!!")
                .nickname(i+"nick")
                .build();
            Member member = signUpService.signUpTest(form);
            members.add(member);
        }

        Archive archive = Archive.builder()
            .id(1L)
            .build();
        Archive savedArchive = archiveRepository.save(archive);

        //when
        IntStream.range(0, 20).forEach(e -> executorService.submit(() -> {
            try {
                try {
                    heartService.createWithLock(members.get(e).getId(), savedArchive.getId());
                } catch (Exception exception) {
                    assertThat(exception).isNull();
                }
            } finally {
                countDownLatch.countDown();
            }
        }));

        countDownLatch.await();

        long heartCnt = archiveRepository.findById(archive.getId()).get().getHeartCnt();

        //then
        assertEquals(20, heartCnt);
    }

}