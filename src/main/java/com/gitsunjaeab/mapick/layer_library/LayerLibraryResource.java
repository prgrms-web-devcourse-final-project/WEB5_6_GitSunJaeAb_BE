package com.gitsunjaeab.mapick.layer_library;

import com.gitsunjaeab.mapick.layer.Layer;
import com.gitsunjaeab.mapick.layer.LayerRepository;
import com.gitsunjaeab.mapick.member.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
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
@RequestMapping(value = "/api/layerLibraries", produces = MediaType.APPLICATION_JSON_VALUE)
public class LayerLibraryResource {

    private final LayerLibraryService layerLibraryService;
    private final MemberRepository memberRepository;
    private final LayerRepository layerRepository;

    public LayerLibraryResource(final LayerLibraryService layerLibraryService,
            final MemberRepository memberRepository, final LayerRepository layerRepository) {
        this.layerLibraryService = layerLibraryService;
        this.memberRepository = memberRepository;
        this.layerRepository = layerRepository;
    }

    @GetMapping
    public ResponseEntity<List<LayerLibraryDTO>> getAllLayerLibraries() {
        return ResponseEntity.ok(layerLibraryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LayerLibraryDTO> getLayerLibrary(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(layerLibraryService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLayerLibrary(
            @RequestBody @Valid final LayerLibraryDTO layerLibraryDTO) {
        final Long createdId = layerLibraryService.create(layerLibraryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLayerLibrary(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final LayerLibraryDTO layerLibraryDTO) {
        layerLibraryService.update(id, layerLibraryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLayerLibrary(@PathVariable(name = "id") final Long id) {
        layerLibraryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/memberValues")
    public ResponseEntity<Map<Long, String>> getMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

    @GetMapping("/layerValues")
    public ResponseEntity<Map<Long, String>> getLayerValues() {
        return ResponseEntity.ok(layerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Layer::getId, Layer::getName)));
    }

}
