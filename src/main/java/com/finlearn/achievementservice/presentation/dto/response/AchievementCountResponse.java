package com.finlearn.achievementservice.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AchievementCountResponse {
    private UUID userId;
    private UUID seasonId;
    private Integer achievementCount;
}
