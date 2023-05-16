package com.green.greenbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private String nickname;
    private String head;
    private String content;
    private long scrapCnt;

    public ReviewResponse toResponse() {
        return ReviewResponse.builder()
                .nickname(nickname)
                .head(head)
                .content(content)
                .scrapCnt(scrapCnt)
                .build();
    }
}