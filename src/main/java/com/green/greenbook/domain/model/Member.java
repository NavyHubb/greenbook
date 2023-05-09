package com.green.greenbook.domain.model;

import com.green.greenbook.domain.form.SignUpForm;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Scrap> scraps
            = new ArrayList<>();

    private String email;
    private String password;
    private String nickname;

    public static Member from(SignUpForm form) {
        return Member.builder()
                .email(form.getEmail())
                .password(form.getPassword())
                .nickname(form.getNickname())
                .build();
    }

}