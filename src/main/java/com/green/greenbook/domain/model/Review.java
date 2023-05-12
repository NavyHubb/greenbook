package com.green.greenbook.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE review SET deleted_at = current_timestamp WHERE review_id = ?")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @Builder.Default
    @OneToMany(mappedBy = "review")
    private List<Scrap> scraps
            = new ArrayList<>();

    private String head;
    private String content;
    private long scrapCnt;
    private LocalDateTime deletedAt;

}