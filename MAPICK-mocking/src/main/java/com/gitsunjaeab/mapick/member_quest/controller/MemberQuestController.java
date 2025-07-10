package com.gitsunjaeab.mapick.member_quest.controller;

import com.gitsunjaeab.mapick.member.entity.Member;
import com.gitsunjaeab.mapick.member.MemberRepository;
import com.gitsunjaeab.mapick.member_quest.dto.MemberQuestDTO;
import com.gitsunjaeab.mapick.member_quest.MemberQuestService;
import com.gitsunjaeab.mapick.quest.entity.Quest;
import com.gitsunjaeab.mapick.quest.QuestRepository;
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
@RequestMapping(value = "/memberQuests", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberQuestController {

    private final MemberQuestService memberQuestService;
    private final MemberRepository memberRepository;
    private final QuestRepository questRepository;

    public MemberQuestController(final MemberQuestService memberQuestService,
            final MemberRepository memberRepository, final QuestRepository questRepository) {
        this.memberQuestService = memberQuestService;
        this.memberRepository = memberRepository;
        this.questRepository = questRepository;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMemberQuest(
        @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
        final Long createdId = memberQuestService.create(memberQuestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<List<MemberQuestDTO>> getAllMemberQuests() {
//        return ResponseEntity.ok(memberQuestService.findAll());
//    }

    // TODO : 멤버 퀘스트 조회 해야되는데, 명세서에 퀘스트참여 Get 이없음(퀘스트증빙자료 get 은 있음)
//    @GetMapping("/{memberQuestsId}")
//    public ResponseEntity<MemberQuestDTO> getMemberQuest(@PathVariable(name = "memberQuestsId") final Long memberQuestsId) {
//        return ResponseEntity.ok(memberQuestService.get(memberQuestsId));
//    }

    // TODO : 멤버 퀘스트 Put 이 필요한지 고민해봐야됨 (퀘스트증빙자료 Put은 가능함)
//    @PutMapping("/{memberQuestsId}")
//    public ResponseEntity<Long> updateMemberQuest(@PathVariable(name = "memberQuestsId") final Long memberQuestsId,
//            @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
//        memberQuestService.update(memberQuestsId, memberQuestDTO);
//        return ResponseEntity.ok(memberQuestsId);
//    }

    // TODO : 멤버 퀘스트 delete 가 필요한지 고민해봐야됨 (퀘스트증빙자료 delete은 가능함)
//    @DeleteMapping("/{memberQuestsId}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteMemberQuest(@PathVariable(name = "memberQuestsId") final Long memberQuestsId) {
//        final ReferencedWarning referencedWarning = memberQuestService.getReferencedWarning(memberQuestsId);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        memberQuestService.delete(memberQuestsId);
//        return ResponseEntity.noContent().build();
//    }

//    @GetMapping("/memberValues")
//    public ResponseEntity<Map<Long, String>> getMemberValues() {
//        return ResponseEntity.ok(memberRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Member::getId, Member::getNickname)));
//    }
//
//    @GetMapping("/questValues")
//    public ResponseEntity<Map<Long, String>> getQuestValues() {
//        return ResponseEntity.ok(questRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Quest::getId, Quest::getTitle)));
//    }

}
