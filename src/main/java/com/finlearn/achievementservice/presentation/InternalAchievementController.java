package com.finlearn.achievementservice.presentation;

import com.finlearn.achievementservice.application.AchievementService;
import com.finlearn.achievementservice.presentation.dto.response.AchievementCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/internal/v1/achievements")
@RequiredArgsConstructor
public class InternalAchievementController {

    private final AchievementService achievementService;

    // season-service 시드머니 산정 시 호출
    @GetMapping("/count")
    public AchievementCountResponse getAchievementCount(
            @RequestParam UUID userId,
            @RequestParam UUID seasonId
    ) {
        long count = achievementService.getMyAchievementCount(userId, seasonId);
        return new AchievementCountResponse(userId, seasonId, (int) count);
    }
}
