package com.green.greenbook.domain.dto;

import com.green.greenbook.domain.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveDto {

    private List<Review> reviewList;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private long subscribeCnt;
    private long likeCnt;

    public ArchiveResponse toResponse() {
        return ArchiveResponse.builder()
                .isbn(this.isbn)
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .subscribeCnt(this.subscribeCnt)
                .likeCnt(this.likeCnt)
                .build();
    }

}