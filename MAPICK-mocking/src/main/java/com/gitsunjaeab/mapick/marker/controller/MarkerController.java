package com.gitsunjaeab.mapick.marker.controller;

import com.gitsunjaeab.mapick.layer.entity.Layer;
import com.gitsunjaeab.mapick.layer.LayerRepository;
import com.gitsunjaeab.mapick.marker.MarkerService;
import com.gitsunjaeab.mapick.marker.dto.MarkerDTO;
import com.gitsunjaeab.mapick.member.entity.Member;
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
@RequestMapping(value = "/markers", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarkerController {

    private final MarkerService markerService;
    private final MemberRepository memberRepository;
    private final LayerRepository layerRepository;

    public MarkerController(final MarkerService markerService,
            final MemberRepository memberRepository, final LayerRepository layerRepository) {
        this.markerService = markerService;
        this.memberRepository = memberRepository;
        this.layerRepository = layerRepository;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMarker(@RequestBody @Valid final MarkerDTO markerDTO) {
        final Long createdId = markerService.create(markerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<List<MarkerDTO>> getAllMarkers() {
//        return ResponseEntity.ok(markerService.findAll());
//    }

    @GetMapping("/{markersId}")
    public ResponseEntity<MarkerDTO> getMarker(@PathVariable(name = "markersId") final Long markersId) {
        return ResponseEntity.ok(markerService.get(markersId));
    }

    @PutMapping("/{markersId}")
    public ResponseEntity<Long> updateMarker(@PathVariable(name = "markersId") final Long markersId,
            @RequestBody @Valid final MarkerDTO markerDTO) {
        markerService.update(markersId, markerDTO);
        return ResponseEntity.ok(markersId);
    }

    @DeleteMapping("/{markersId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMarker(@PathVariable(name = "markersId") final Long markersId) {
        final ReferencedWarning referencedWarning = markerService.getReferencedWarning(markersId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        markerService.delete(markersId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/memberValues")
//    public ResponseEntity<Map<Long, String>> getMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }
//
//    @GetMapping("/layerValues")
//    public ResponseEntity<Map<Long, String>> getLayerValues() {
//        return ResponseEntity.ok(layerRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Layer::getId, Layer::getName)));
//    }

}
