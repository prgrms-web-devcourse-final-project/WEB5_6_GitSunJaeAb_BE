package com.gitsunjaeab.mapick.quest.controller;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.quest.dto.QuestDTO;
import com.gitsunjaeab.mapick.quest.QuestService;
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
@RequestMapping(value = "/quests", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestController {

    private final QuestService questService;
    private final MemberRepository memberRepository;

    public QuestController(final QuestService questService, final MemberRepository memberRepository) {
        this.questService = questService;
        this.memberRepository = memberRepository;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createQuest(@RequestBody @Valid final QuestDTO questDTO) {
        final Long createdId = questService.create(questDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<List<QuestDTO>> getAllQuests() {
//        return ResponseEntity.ok(questService.findAll());
//    }

    @GetMapping("/{questsId}")
    public ResponseEntity<QuestDTO> getQuest(@PathVariable(name = "questsId") final Long questsId) {
        return ResponseEntity.ok(questService.get(questsId));
    }

    @PutMapping("/{questsId}")
    public ResponseEntity<Long> updateQuest(@PathVariable(name = "questsId") final Long questsId,
            @RequestBody @Valid final QuestDTO questDTO) {
        questService.update(questsId, questDTO);
        return ResponseEntity.ok(questsId);
    }

    @DeleteMapping("/{questsId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuest(@PathVariable(name = "questsId") final Long questsId) {
        final ReferencedWarning referencedWarning = questService.getReferencedWarning(questsId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        questService.delete(questsId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/memberValues")
//    public ResponseEntity<Map<Long, String>> getMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }

}
