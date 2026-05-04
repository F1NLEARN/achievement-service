package com.finlearn.achievementservice.presentation;

import com.finlearn.achievementservice.application.AchievementService;
import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.presentation.dto.response.AchievementListResponse;
import com.finlearn.achievementservice.presentation.dto.response.AchievementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    /**
     * GET /api/v1/achievements
     * 전체 업적 목록 조회
     */
    @GetMapping
    public AchievementListResponse getAllAchievements(
            @RequestParam(required = false) AchievementCategory category,
            @RequestParam(required = false) AchievementDifficulty difficulty
    ) {
        List<Achievement> achievements = achievementService.getAllAchievements(category, difficulty);
        List<AchievementResponse> responses = achievements.stream()
                .map(AchievementResponse::from)
                .toList();
        return new AchievementListResponse(responses);
    }
}
