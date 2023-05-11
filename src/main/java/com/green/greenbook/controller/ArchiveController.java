package com.green.greenbook.controller;

import com.green.greenbook.domain.form.ArchiveForm;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/archive")
public class ArchiveController {

    private final ArchiveService archiveService;

    @PostMapping
    public ResponseEntity<Archive> create(@RequestBody ArchiveForm form) {
        return ResponseEntity.ok(archiveService.create(form));
    }

    @GetMapping("/{archiveId}")
    public ResponseEntity<Archive> get(@PathVariable Long archiveId) {
        return ResponseEntity.ok(archiveService.get(archiveId));
    }

    @PutMapping("/{archiveId}")
    public ResponseEntity<Archive> update(@PathVariable Long archiveId, @RequestBody ArchiveForm form) {
        return ResponseEntity.ok(archiveService.update(archiveId, form));
    }

    @DeleteMapping("/{archiveId}")
    public ResponseEntity<String> delete(@PathVariable Long archiveId) {
        return ResponseEntity.ok(archiveService.delete(archiveId));
    }

}