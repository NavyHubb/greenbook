package com.green.greenbook.domain.dto;

import com.green.greenbook.domain.model.Review;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveResponse {

    private List<Review> reviewList;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private long subscribeCnt;
    private long likeCnt;

}