package com.finlearn.achievementservice.application.port;

import java.util.UUID;

public interface AchievementEventPublisher {

    /** 업적 달성 이벤트 발행 → ranking-service 수신: ACHIEVEMENT 점수 +1 */
    void publishAchievementUnlocked(UUID userId, UUID seasonId,
                                     Integer seasonNumber,
                                     String userNickname,
                                     String userProfileImage,
                                     String achievementName,
                                     String achievementCategory,
                                     String achievementDifficulty);
}
