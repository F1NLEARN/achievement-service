package com.finlearn.achievementservice.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class AchievementListResponse {
    private List<AchievementResponse> achievements;
}
