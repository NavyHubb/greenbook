package com.green.greenbook.domain.model;

import com.green.greenbook.domain.dto.MemberResponseDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String email;
    private String password;
    private String nickname;

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .id(member.id)
                .email(member.email)
                .password(member.password)
                .nickname(member.nickname)
                .build();
    }

}