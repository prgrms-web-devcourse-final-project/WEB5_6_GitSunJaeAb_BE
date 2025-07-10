package com.gitsunjaeab.mapick.roadmap.controller;

import com.gitsunjaeab.mapick.roadmap.RoadmapRepository;
import com.gitsunjaeab.mapick.roadmap.RoadmapService;
import com.gitsunjaeab.mapick.roadmap.dto.RoadmapDTO;
import com.gitsunjaeab.mapick.roadmap.entity.Roadmap;
import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.util.ReferencedException;
import com.gitsunjaeab.mapick.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/maps", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoadmapController {

    private final RoadmapService roadmapService;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;

    public RoadmapController(final RoadmapService roadmapService, final MemberRepository memberRepository,
            final RoadmapRepository roadmapRepository) {
        this.roadmapService = roadmapService;
        this.memberRepository = memberRepository;
        this.roadmapRepository = roadmapRepository;
    }

    // TODO 개인 지도 생성 >> /personal

    // TODO 공유 지도 생성 >> /shared

    // TODO 특정 회원의 지도 목록 조회

    // 특정 지도 상세 조회
    @GetMapping("/{mapId}")
    public ResponseEntity<RoadmapDTO> getMap(@PathVariable(name = "mapId") final Long mapId) {
        return ResponseEntity.ok(roadmapService.get(mapId));
    }

    // 지도 수정
    @PutMapping("/{mapId}")
    public ResponseEntity<Long> updateMap(@PathVariable(name = "mapId") final Long mapId,
            @RequestBody @Valid final RoadmapDTO roadmapDTO) {
        roadmapService.update(mapId, roadmapDTO);
        return ResponseEntity.ok(mapId);
    }

    // 지도 삭제
    @DeleteMapping("/{mapId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMap(@PathVariable(name = "mapId") final Long mapId) {
        final ReferencedWarning referencedWarning = roadmapService.getReferencedWarning(mapId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        roadmapService.delete(mapId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public ResponseEntity<List<MapDTO>> getAllMaps() {
//        return ResponseEntity.ok(mapService.findAll());
//    }

//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createMap(@RequestBody @Valid final MapDTO mapDTO) {
//        final Long createdId = mapService.create(mapDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }

//    @GetMapping("/memberValues")
//    public ResponseEntity<java.util.Map<Long, String>> getMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }
//
//    @GetMapping("/originalMapValues")
//    public ResponseEntity<java.util.Map<Long, String>> getOriginalMapValues() {
//        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Map::getId, Map::getTitle)));
//    }

}
