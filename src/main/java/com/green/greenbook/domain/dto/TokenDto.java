package com.green.greenbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String token;

    public static TokenDto from(String token) {
        return TokenDto.builder()
            .token(token)
            .build();
    }

}