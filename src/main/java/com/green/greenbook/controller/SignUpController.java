package com.green.greenbook.controller;

import com.green.greenbook.domain.form.SignUpForm;
import com.green.greenbook.exception.ErrorResponse;
import com.green.greenbook.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/signUp")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<Object> signUp(@RequestBody @Valid SignUpForm form, Errors errors) {
        if (errors.hasErrors()) {
            validate(errors);
        }

        form.encryptPassword(form.getPassword());

        return ResponseEntity.ok(signUpService.signUp(form));
    }

    private ResponseEntity<Object> validate(Errors errors) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        errors.getAllErrors().stream().forEach(e -> errorResponses.add(ErrorResponse.of(e)));

        return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
    }

}
