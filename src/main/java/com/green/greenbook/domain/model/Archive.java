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
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_id")
    private Long id;

    @OneToMany(mappedBy = "archive")
    private List<Review> reviewList =
            new ArrayList<>();

    private String title;
    private String author;
    private String publisher;
    private int isbn;
    private String description;
    private String imageUrl;
    private long hits;

}
