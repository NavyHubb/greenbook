package com.green.greenbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveDto {

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private long heartCnt;

    public ArchiveResponse toResponse() {
        return ArchiveResponse.builder()
                .isbn(this.isbn)
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .heartCnt(this.heartCnt)
                .build();
    }

}