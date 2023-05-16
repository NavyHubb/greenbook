package com.green.greenbook.domain.model;

import com.green.greenbook.domain.dto.ReviewDto;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "archive_id")
    private Archive archive;

//    @Builder.Default
//    @OneToMany(mappedBy = "review")
//    private List<Scrap> scraps
//            = new ArrayList<>();

    private String head;
    private String content;
    private long scrapCnt;
    private LocalDateTime deletedAt;

    public Review update(String head, String content) {
        this.head = head;
        this.content = content;

        return this;
    }

    public ReviewDto toDto() {
        return ReviewDto.builder()
                .nickname(this.member.getNickname())
                .head(this.head)
                .content(this.content)
                .build();
    }

    public void plusScrapCnt() {
        this.scrapCnt++;
    }

    public void minusScrapCnt() {
        if (this.scrapCnt > 0) {
            this.scrapCnt--;
        }
    }

}