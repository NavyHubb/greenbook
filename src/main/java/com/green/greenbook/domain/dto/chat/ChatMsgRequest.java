package com.green.greenbook.domain.dto.chat;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsgRequest {

    @NotEmpty
    @Length(max = 100)
    private String message;

}