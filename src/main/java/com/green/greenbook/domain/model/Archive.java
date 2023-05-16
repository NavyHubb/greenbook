package com.green.greenbook.domain.model;

import com.green.greenbook.domain.dto.ArchiveResponse;
import com.green.greenbook.domain.dto.ArchiveDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE archive SET deleted_at = now() WHERE archive_id = ?")
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
    private long heartCnt;
    private LocalDateTime deletedAt;

    public ArchiveDto toDto() {
        return ArchiveDto.builder()
                .reviewList(this.reviewList)
                .isbn(this.isbn)
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .heartCnt(0)
                .build();
    }

    public ArchiveResponse toResponse() {
        return ArchiveResponse.builder()
            .reviewList(this.reviewList)
            .isbn(this.isbn)
            .title(this.title)
            .author(this.author)
            .publisher(this.publisher)
            .heartCnt(this.heartCnt)
            .build();
    }

    public Archive update(String title, String author, String publisher) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;

        return this;
    }

    public void plusHeart() {
        this.heartCnt++;
    }

    public void minusHeart() {
        if (this.heartCnt > 0) {
            this.heartCnt--;
        }
    }

}