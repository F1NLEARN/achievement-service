package com.finlearn.achievementservice.infrastructure.kafka;

import com.finlearn.achievementservice.application.port.AchievementEventPublisher;
import com.finlearn.achievementservice.infrastructure.kafka.event.AchievementUnlockedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 업적 도메인 이벤트 Kafka 발행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementEventProducer implements AchievementEventPublisher {

    @Value("${kafka.topics.achievement.unlocked}")
    private String topicAchievementUnlocked;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishAchievementUnlocked(UUID userId, UUID seasonId,
                                            Integer seasonNumber,
                                            String userNickname,
                                            String userProfileImage,
                                            String achievementName,
                                            String achievementCategory,
                                            String achievementDifficulty) {
        AchievementUnlockedEvent event = new AchievementUnlockedEvent(
                userId, seasonId, seasonNumber, userNickname, userProfileImage,
                achievementName, achievementCategory, achievementDifficulty);
        log.info("[Kafka] AchievementUnlocked 발행: userId={}, name={}", userId, achievementName);
        kafkaTemplate.send(topicAchievementUnlocked, userId.toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka] AchievementUnlocked 발행 실패: {}", ex.getMessage());
                    }
                });
    }
}
