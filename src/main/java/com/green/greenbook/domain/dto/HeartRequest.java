package com.green.greenbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeartRequest {

    @NotNull
    private Long memberId;

    @NotNull
    private Long archiveId;

}