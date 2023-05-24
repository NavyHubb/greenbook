package com.green.greenbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.green.greenbook.domain.dto.MemberResponseDto;
import com.green.greenbook.domain.form.SignUpForm;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.domain.model.Member;
import com.green.greenbook.repository.ArchiveRepository;
import com.green.greenbook.repository.HeartRepository;
import com.green.greenbook.repository.MemberRepository;
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
import org.springframework.transaction.annotation.Transactional;

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
        countDownLatch = new CountDownLatch(20);  // 몇개의 스레드가 끝나면 다음 스레드를 시작할지 정한다
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

        IntStream.range(0, 20).forEach(e -> executorService.submit(() -> {
            try {
                try {
                    heartService.create(members.get(e).getId(), savedArchive.getId());
                } catch (Exception exception) {
                    assertThat(exception).isNull();
                }
            } finally {
                countDownLatch.countDown();  // 스레드가 끝날 때 마다 카운트를 감소한다
            }
        }));

        countDownLatch.await();  // 카운트가 0이되면 대기가 풀리고 이후 스레드가 실행되게 된다

        long heartCnt = archiveRepository.findById(archive.getId()).get().getHeartCnt();

        assertEquals(20, heartCnt);
    }

}
