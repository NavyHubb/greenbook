package com.green.greenbook.domain.model;

import com.green.greenbook.domain.dto.ArchiveResponseDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE archive SET deleted_at = current_timestamp WHERE archive_id = ?")
@Where(clause = "deleted_at is null")
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_id")
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "archive")
    private List<Review> reviewList =
            new ArrayList<>();

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private long subscribeCnt;
    private long likeCnt;
    private LocalDateTime deletedAt;

    public static Archive from(ArchiveResponseDto responseDto) {
        return Archive.builder()
            .isbn(responseDto.getIsbn())
            .title(responseDto.getTitle())
            .author(responseDto.getAuthor())
            .publisher(responseDto.getPublisher())
            .subscribeCnt(0)
            .likeCnt(0)
            .build();
    }

    public ArchiveResponseDto toServiceDto() {
        return ArchiveResponseDto.builder()
            .isbn(this.isbn)
            .title(this.title)
            .author(this.author)
            .publisher(this.publisher)
            .build();
    }

    public Archive update(ArchiveResponseDto responseDto) {
         this.title = responseDto.getTitle();
         this.author = responseDto.getAuthor();
         this.publisher = responseDto.getPublisher();

         return this;
    }

}