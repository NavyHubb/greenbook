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
public class ArchiveUpdateRequest {

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String publisher;

}