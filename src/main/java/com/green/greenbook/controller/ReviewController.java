package com.green.greenbook.controller;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.dto.MemberDto;
import com.green.greenbook.domain.dto.ReviewRequest;
import com.green.greenbook.domain.dto.ReviewResponse;
import com.green.greenbook.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader(name = TOKEN_NAME) String token,
                                    @RequestParam Long archiveId,
                                    @RequestBody ReviewRequest request) {
        MemberDto dto = provider.getMemberDto(token);
        reviewService.create(archiveId, dto.getId(), request.getHead(), request.getContent());

        return ResponseEntity.ok("리뷰가 저장되었습니다.");
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> get(@PathVariable Long reviewId){
        return ResponseEntity.ok(reviewService.get(reviewId).toResponse());
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@RequestHeader(name = TOKEN_NAME) String token,
                                    @PathVariable Long reviewId,
                                    @RequestBody ReviewRequest request) {
        MemberDto dto = provider.getMemberDto(token);

        return ResponseEntity.ok(reviewService.update(reviewId, dto.getId(), request.getHead(), request.getContent()));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@RequestHeader(name = TOKEN_NAME) String token,
                                    @PathVariable Long reviewId) {
        MemberDto dto = provider.getMemberDto(token);
        reviewService.delete(reviewId, dto.getId());

        return ResponseEntity.ok("삭제 처리되었습니다.");
    }

}