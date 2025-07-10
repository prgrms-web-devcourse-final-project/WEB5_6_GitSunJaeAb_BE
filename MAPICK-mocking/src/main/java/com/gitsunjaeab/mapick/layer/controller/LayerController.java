package com.gitsunjaeab.mapick.layer.controller;

import com.gitsunjaeab.mapick.layer.LayerService;
import com.gitsunjaeab.mapick.layer.dto.LayerDTO;
import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/layers", produces = MediaType.APPLICATION_JSON_VALUE)
public class LayerController {

    private final LayerService layerService;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;

    public LayerController(final LayerService layerService, final MemberRepository memberRepository,
            final RoadmapRepository roadmapRepository) {
        this.layerService = layerService;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
    }

    // TODO 특정 회원의 레이어 조회

    // 특정 레이어 조회
    @GetMapping("/{layerId}")
    public ResponseEntity<LayerDTO> getLayer(@PathVariable(name = "layerId") final Long layerId) {
        return ResponseEntity.ok(layerService.get(layerId));
    }

    // 레이어 생성
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLayer(@RequestBody @Valid final LayerDTO layerDTO) {
        final Long createdId = layerService.create(layerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    // 특정 레이어 수정
    @PutMapping("/{layerId}")
    public ResponseEntity<Long> updateLayer(@PathVariable(name = "layerId") final Long layerId,
            @RequestBody @Valid final LayerDTO layerDTO) {
        layerService.update(layerId, layerDTO);
        return ResponseEntity.ok(layerId);
    }

    // 특정 레이어 삭제
    @DeleteMapping("/{layerId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLayer(@PathVariable(name = "layerId") final Long layerId) {
        final ReferencedWarning referencedWarning = layerService.getReferencedWarning(layerId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        layerService.delete(layerId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public ResponseEntity<List<LayerDTO>> getAllLayers() {
//        return ResponseEntity.ok(layerService.findAll());
//    }

//
//    @GetMapping("/memberValues")
//    public ResponseEntity<Map<Long, String>> getMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }
//
//    @GetMapping("/mapValues")
//    public ResponseEntity<Map<Long, String>> getMapValues() {
//        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(com.gitsunjaeab.mapick.map.entity.Map::getId, com.gitsunjaeab.mapick.map.entity.Map::getTitle)));
//    }

}
