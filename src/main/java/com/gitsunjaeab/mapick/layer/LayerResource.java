package com.gitsunjaeab.mapick.layer;

import com.gitsunjaeab.mapick.map.MapRepository;
import com.gitsunjaeab.mapick.member.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
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
@RequestMapping(value = "/api/layers", produces = MediaType.APPLICATION_JSON_VALUE)
public class LayerResource {

    private final LayerService layerService;
    private final MemberRepository memberRepository;
    private final MapRepository mapRepository;

    public LayerResource(final LayerService layerService, final MemberRepository memberRepository,
            final MapRepository mapRepository) {
        this.layerService = layerService;
        this.memberRepository = memberRepository;
        this.mapRepository = mapRepository;
    }

    @GetMapping
    public ResponseEntity<List<LayerDTO>> getAllLayers() {
        return ResponseEntity.ok(layerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LayerDTO> getLayer(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(layerService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLayer(@RequestBody @Valid final LayerDTO layerDTO) {
        final Long createdId = layerService.create(layerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLayer(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final LayerDTO layerDTO) {
        layerService.update(id, layerDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLayer(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = layerService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        layerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/memberValues")
    public ResponseEntity<Map<Long, String>> getMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

    @GetMapping("/mapValues")
    public ResponseEntity<Map<Long, String>> getMapValues() {
        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(com.gitsunjaeab.mapick.map.Map::getId, com.gitsunjaeab.mapick.map.Map::getTitle)));
    }

}
