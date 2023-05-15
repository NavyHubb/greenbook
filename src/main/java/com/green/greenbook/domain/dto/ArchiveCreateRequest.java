package com.green.greenbook.domain.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveCreateRequest {

    @NotNull
    private String isbn;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String publisher;

}