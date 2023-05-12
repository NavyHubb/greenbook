package com.green.greenbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveRequestDto {

    private String title;
    private String author;
    private String publisher;

    public ArchiveResponseDto toServiceDto() {
        return ArchiveResponseDto.builder()
            .title(this.title)
            .author(this.author)
            .publisher(this.publisher)
            .build();
    }

}