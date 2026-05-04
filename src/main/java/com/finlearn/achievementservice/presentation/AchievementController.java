package com.finlearn.achievementservice.presentation;

import com.finlearn.achievementservice.application.AchievementService;
import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.UserAchievement;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.presentation.dto.response.AchievementListResponse;
import com.finlearn.achievementservice.presentation.dto.response.AchievementResponse;
import com.finlearn.achievementservice.presentation.dto.response.UserAchievementListResponse;
import com.finlearn.achievementservice.presentation.dto.response.UserAchievementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

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

    @GetMapping("/me")
    public UserAchievementListResponse getMyAchievements(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(required = false) UUID seasonId
    ) {
        List<UserAchievement> userAchievements = achievementService.getMyAchievements(userId, seasonId);
        List<UserAchievementResponse> responses = userAchievements.stream()
                .map(UserAchievementResponse::from)
                .toList();
        return new UserAchievementListResponse(responses);
    }
}
