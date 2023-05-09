package com.green.greenbook.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String field;
    private String message;

    public static ErrorResponse of(ObjectError e) {
        return ErrorResponse.builder()
                .field(((FieldError)e).getField())
                .message(e.getDefaultMessage())
                .build();
    }

}