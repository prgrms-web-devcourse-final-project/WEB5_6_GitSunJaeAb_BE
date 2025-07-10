//package com.gitsunjaeab.mapick.map_editor.controller;
//
//import com.gitsunjaeab.mapick.map.MapRepository;
//import com.gitsunjaeab.mapick.map_editor.MapEditorService;
//import com.gitsunjaeab.mapick.map_editor.dto.MapEditorDTO;
//import com.gitsunjaeab.mapick.member.Member;
//import com.gitsunjaeab.mapick.member.MemberRepository;
//import com.gitsunjaeab.mapick.util.CustomCollectors;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import jakarta.validation.Valid;
//import java.util.List;
//import java.util.Map;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping(value = "/api/mapEditors", produces = MediaType.APPLICATION_JSON_VALUE)
//public class MapEditorController {
//
//    private final MapEditorService mapEditorService;
//    private final MapRepository mapRepository;
//    private final MemberRepository memberRepository;
//
//    public MapEditorController(final MapEditorService mapEditorService,
//            final MapRepository mapRepository, final MemberRepository memberRepository) {
//        this.mapEditorService = mapEditorService;
//        this.mapRepository = mapRepository;
//        this.memberRepository = memberRepository;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<MapEditorDTO>> getAllMapEditors() {
//        return ResponseEntity.ok(mapEditorService.findAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<MapEditorDTO> getMapEditor(@PathVariable(name = "id") final Long id) {
//        return ResponseEntity.ok(mapEditorService.get(id));
//    }
//
//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createMapEditor(
//            @RequestBody @Valid final MapEditorDTO mapEditorDTO) {
//        final Long createdId = mapEditorService.create(mapEditorDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Long> updateMapEditor(@PathVariable(name = "id") final Long id,
//            @RequestBody @Valid final MapEditorDTO mapEditorDTO) {
//        mapEditorService.update(id, mapEditorDTO);
//        return ResponseEntity.ok(id);
//    }
//
//    @DeleteMapping("/{id}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteMapEditor(@PathVariable(name = "id") final Long id) {
//        mapEditorService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/mapValues")
//    public ResponseEntity<Map<Long, String>> getMapValues() {
//        return ResponseEntity.ok(mapRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(com.gitsunjaeab.mapick.map.entity.Map::getId, com.gitsunjaeab.mapick.map.entity.Map::getTitle)));
//    }
//
//    @GetMapping("/memberValues")
//    public ResponseEntity<Map<Long, String>> getMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }
//
//    @GetMapping("/invitedByValues")
//    public ResponseEntity<Map<Long, String>> getInvitedByValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }
//
//}
