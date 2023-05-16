package com.green.greenbook.controller;

import com.green.greenbook.domain.dto.ArchiveCreateRequest;
import com.green.greenbook.domain.dto.ArchiveResponse;
import com.green.greenbook.domain.dto.ArchiveUpdateRequest;
import com.green.greenbook.domain.model.Archive;
import com.green.greenbook.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/archive")
public class ArchiveController {

    private final ArchiveService archiveService;

    @PostMapping
    public ResponseEntity<ArchiveResponse> create(@RequestBody @Valid ArchiveCreateRequest request) {
        return ResponseEntity.ok(archiveService.create(
                request.getIsbn(), request.getTitle(), request.getAuthor(), request.getPublisher())
                .toResponse());
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