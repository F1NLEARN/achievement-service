package com.finlearn.achievementservice.presentation.dto.response;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class AchievementResponse {
    private UUID achievementId;
    private String name;
    private AchievementCategory category;
    private AchievementDifficulty difficulty;
    private ConditionType conditionType;
    private BigDecimal conditionValue;

    public static AchievementResponse from(Achievement achievement) {
        return AchievementResponse.builder()
                .achievementId(achievement.getAchievementId())
                .name(achievement.getName())
                .category(achievement.getCategory())
                .difficulty(achievement.getDifficulty())
                .conditionType(achievement.getConditionType())
                .conditionValue(achievement.getConditionValue())
                .build();
    }
}
