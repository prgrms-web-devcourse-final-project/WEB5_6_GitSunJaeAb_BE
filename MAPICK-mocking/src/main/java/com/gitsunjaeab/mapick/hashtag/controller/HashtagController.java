package com.gitsunjaeab.mapick.hashtag.controller;

import com.gitsunjaeab.mapick.hashtag.HashtagService;
import com.gitsunjaeab.mapick.hashtag.dto.HashtagDTO;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/hashtags", produces = MediaType.APPLICATION_JSON_VALUE)
public class HashtagController {

    private final HashtagService hashtagService;

    public HashtagController(final HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    @GetMapping
    public ResponseEntity<List<HashtagDTO>> getAllHashtags() {
        return ResponseEntity.ok(hashtagService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HashtagDTO> getHashtag(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(hashtagService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createHashtag(@RequestBody @Valid final HashtagDTO hashtagDTO) {
        final Long createdId = hashtagService.create(hashtagDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateHashtag(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final HashtagDTO hashtagDTO) {
        hashtagService.update(id, hashtagDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteHashtag(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = hashtagService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        hashtagService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
