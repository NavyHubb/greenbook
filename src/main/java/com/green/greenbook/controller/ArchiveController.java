package com.green.greenbook.controller;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.dto.ArchiveCreateRequest;
import com.green.greenbook.domain.dto.ArchiveDto;
import com.green.greenbook.domain.dto.ArchiveResponse;
import com.green.greenbook.domain.dto.ArchiveUpdateRequest;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/archive")
public class ArchiveController {

    private final ArchiveService archiveService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping
    public ResponseEntity<ArchiveResponse> create(@RequestHeader(name = TOKEN_NAME) String token,
                                                  @RequestBody @Valid ArchiveCreateRequest request) {
        return ResponseEntity.ok(archiveService.create(
                request.getIsbn(), request.getTitle(), request.getAuthor(), request.getPublisher())
                .toResponse());
    }

    @GetMapping
    public Page<ArchiveDto> getAll(
        @PageableDefault(size = 5, sort = "heartCnt", direction = Sort.Direction.DESC) Pageable pageable) {
        return archiveService.getAll(pageable);
    }

    @GetMapping("/{archiveId}")
    public ResponseEntity<Archive> get(@PathVariable Long archiveId) {
        return ResponseEntity.ok(archiveService.get(archiveId));
    }

    @PutMapping("/{archiveId}")
    public ResponseEntity<ArchiveResponse> update(@PathVariable Long archiveId, @RequestBody ArchiveUpdateRequest requestDto) {
        return ResponseEntity.ok(archiveService.update(archiveId, requestDto.getTitle(), requestDto.getAuthor(), requestDto.getPublisher()));
    }

    @DeleteMapping("/{archiveId}")
    public ResponseEntity<String> delete(@PathVariable Long archiveId) {
        return ResponseEntity.ok(archiveService.delete(archiveId));
    }

}