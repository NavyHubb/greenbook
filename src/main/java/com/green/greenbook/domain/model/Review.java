package com.green.greenbook.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @OneToMany(mappedBy = "review")
    private List<Scrap> scraps
            = new ArrayList<>();

    private String head;
    private String content;
    private long scrap_cnt;

}
