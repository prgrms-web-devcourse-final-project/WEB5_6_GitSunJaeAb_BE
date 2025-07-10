package com.gitsunjaeab.mapick.map;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.CustomCollectors;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/api/maps", produces = MediaType.APPLICATION_JSON_VALUE)
public class MapResource {

    private final MapService mapService;
    private final MemberRepository memberRepository;
    private final MapRepository mapRepository;

    public MapResource(final MapService mapService, final MemberRepository memberRepository,
            final MapRepository mapRepository) {
        this.mapService = mapService;
        this.memberRepository = memberRepository;
        this.mapRepository = mapRepository;
    }

    @GetMapping
    public ResponseEntity<List<MapDTO>> getAllMaps() {
        return ResponseEntity.ok(mapService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapDTO> getMap(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(mapService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMap(@RequestBody @Valid final MapDTO mapDTO) {
        final Long createdId = mapService.create(mapDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMap(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MapDTO mapDTO) {
        mapService.update(id, mapDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMap(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = mapService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        mapService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/memberValues")
    public ResponseEntity<java.util.Map<Long, String>> getMemberValues() {
        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
    }

    @GetMapping("/originalMapValues")
    public ResponseEntity<java.util.Map<Long, String>> getOriginalMapValues() {
        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Map::getId, Map::getTitle)));
    }

}
