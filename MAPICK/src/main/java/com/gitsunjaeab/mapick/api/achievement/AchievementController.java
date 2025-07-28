package com.gitsunjaeab.mapick.api.achievement;

import com.gitsunjaeab.mapick.api.achievement.dto.AchievementDTO;
import com.gitsunjaeab.mapick.api.achievement.dto.AchievementListResponse;
import com.gitsunjaeab.mapick.api.achievement.dto.MemberAchievementDTO;
import com.gitsunjaeab.mapick.api.achievement.dto.MemberAchievementListResponse;
import com.gitsunjaeab.mapick.application.achievement.AchievementService;
import com.gitsunjaeab.mapick.domain.auth.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "업적 관리 API")
@RestController
@RequestMapping(value = "/achievements", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    // 전체 업적 조회
    @GetMapping
    @Operation(summary = "전체 업적 조회", description = "[관리자/사용자] 전체 업적 목록을 조회")
    public ResponseEntity<AchievementListResponse> getAchievements() {
        List<AchievementDTO> achievements = achievementService.findAll();
        AchievementListResponse response = AchievementListResponse.getList(achievements);

        return ResponseEntity.ok(response);
    }

    // 사용자 취득 업적 조회
    @GetMapping("/member")
    @Operation(summary = "특정 사용자의 업적 조회", description = "[사용자] 해당 사용자가 취득한 업적 전체 조회")
    public ResponseEntity<MemberAchievementListResponse> getMemberAchievements(
        @AuthenticationPrincipal Principal principal
    ) {

        Long memberId = principal.getMember().getId();
        List<MemberAchievementDTO> achievements = achievementService.findMemberAchievements(memberId);
        MemberAchievementListResponse response = MemberAchievementListResponse.getList(achievements);


        return ResponseEntity.ok(response);
    }

}
