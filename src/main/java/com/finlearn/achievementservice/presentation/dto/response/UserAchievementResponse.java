package com.finlearn.achievementservice.presentation.dto.response;

import com.finlearn.achievementservice.domain.UserAchievement;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserAchievementResponse {

    private UUID userAchievementId;
    private UUID achievementId;
    private String name;
    private AchievementCategory category;
    private AchievementDifficulty difficulty;
    private LocalDateTime achievedAt;
    private UUID seasonId;

    public static UserAchievementResponse from(UserAchievement ua) {
        return UserAchievementResponse.builder()
                .userAchievementId(ua.getUserAchievementId())
                .achievementId(ua.getAchievementId())
                .name(ua.getAchievementName())
                .category(ua.getAchievementCategory())
                .difficulty(ua.getAchievementDifficulty())
                .achievedAt(ua.getAchievedAt())
                .seasonId(ua.getSeasonId())
                .build();
    }
}
