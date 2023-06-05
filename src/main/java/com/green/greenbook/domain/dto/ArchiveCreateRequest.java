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

    @NotNull(message = "ISBN은 필수 입력값입니다.")
    private String isbn;

    @NotNull(message = "도서명은 필수 입력값입니다.")
    private String title;

    @NotNull(message = "저자는 필수 입력값입니다.")
    private String author;

    @NotNull(message = "출판사는 필수 입력값입니다.")
    private String publisher;

}