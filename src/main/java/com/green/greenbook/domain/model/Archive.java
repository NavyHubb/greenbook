package com.green.greenbook.domain.model;

import com.green.greenbook.domain.form.ArchiveForm;
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

    private String title;
    private String author;
    private String publisher;
    private long subscribeCnt;
    private long likeCnt;
    private LocalDateTime deletedAt;

    public static Archive from(ArchiveForm form) {
        return Archive.builder()
            .title(form.getTitle())
            .author(form.getAuthor())
            .publisher(form.getPublisher())
            .subscribeCnt(0)
            .likeCnt(0)
            .build();
    }

    public Archive update(ArchiveForm form) {
         this.title = form.getTitle();
         this.author = form.getAuthor();
         this.publisher = form.getPublisher();

         return this;
    }

}