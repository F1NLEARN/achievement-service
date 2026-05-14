package com.finlearn.achievementservice.presentation;

import com.finlearn.achievementservice.application.AchievementEvaluationService;
import com.finlearn.achievementservice.application.AchievementService;
import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.domain.UserAchievement;
import com.finlearn.achievementservice.presentation.dto.request.AchieveRequest;
import com.finlearn.achievementservice.presentation.dto.response.AchievementCountResponse;
import com.finlearn.achievementservice.presentation.dto.response.UserAchievementListResponse;
import com.finlearn.achievementservice.presentation.dto.response.UserAchievementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

// 서비스 내부 전용 API
@RestController
@RequestMapping("/internal/v1/achievements")
@RequiredArgsConstructor
public class InternalAchievementController {

    private final AchievementService achievementService;
    private final AchievementEvaluationService achievementEvaluationService;

    /**
     * GET /internal/v1/achievements/count
     * 시즌 내 업적 달성 수 조회
     */
    @GetMapping("/count")
    public ResponseEntity<AchievementCountResponse> getAchievementCount(
            @RequestParam UUID userId,
            @RequestParam UUID seasonId
    ) {
        long count = achievementService.getMyAchievementCount(userId, seasonId);
        return ResponseEntity.ok(new AchievementCountResponse(userId, seasonId, (int) count));
    }

    /**
     * POST /internal/v1/achievements/achieve
     * 투자 변경 데이터를 기반으로 업적 조건을 판정하고 달성된 업적 목록을 반환
     */
    @PostMapping("/achieve")
    public ResponseEntity<UserAchievementListResponse> achieve(
            @Valid @RequestBody AchieveRequest request
    ) {
        EvaluateAchievementsCommand command = EvaluateAchievementsCommand.builder()
                .userId(request.getUserId())
                .seasonId(request.getSeasonId())
                .seasonNumber(request.getSeasonNumber())
                .tradeType(request.getTradeType())
                .assetType(request.getAssetType())
                .holdCount(request.getHoldCount())
                .returnRate(request.getReturnRate())
                .userNickname(request.getUserNickname())
                .build();

        List<UserAchievement> newlyAchieved = achievementEvaluationService.evaluateAchievements(command);
        List<UserAchievementResponse> responses = newlyAchieved.stream()
                .map(UserAchievementResponse::from)
                .toList();
        return ResponseEntity.ok(new UserAchievementListResponse(responses));
    }
}
