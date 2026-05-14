package com.finlearn.achievementservice.infrastructure.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

/** 업적 달성 시 발행 → 랭킹 서비스가 수신해 ACHIEVEMENT 점수 +1
 *
 * topic: achievement.unlocked */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementUnlockedEvent {
    private UUID userId;
    private UUID seasonId;
    private Integer seasonNumber;
    private String userNickname;
    private String userProfileImage;
    private String achievementName;
    private String achievementCategory;
    private String achievementDifficulty;
}
