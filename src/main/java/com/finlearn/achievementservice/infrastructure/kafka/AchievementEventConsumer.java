package com.finlearn.achievementservice.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlearn.achievementservice.application.AchievementEvaluationService;
import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.infrastructure.kafka.event.InvestmentChangedEvent;
import com.finlearn.achievementservice.infrastructure.kafka.event.UserProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 업적 도메인 이벤트 Kafka 수신
 * 수신한 이벤트를 Application Command로 변환 후 AchievementEvaluationService에 전달
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementEventConsumer {

    private final AchievementEvaluationService achievementEvaluationService;
    private final ObjectMapper objectMapper;

    /**
     * investment.changed → 업적 조건 판정
     * InvestmentChanged 이벤트를 EvaluateAchievementsCommand로 변환 후 전달
     */
    @KafkaListener(
            topics = "${kafka.topics.investment.changed}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleInvestmentChanged(Map<String, Object> payload) {
        try {
            InvestmentChangedEvent event = objectMapper.convertValue(payload, InvestmentChangedEvent.class);
            EvaluateAchievementsCommand command = EvaluateAchievementsCommand.builder()
                    .userId(event.getUserId())
                    .seasonId(event.getSeasonId())
                    .seasonNumber(event.getSeasonNumber())
                    .tradeType(event.getTradeType())
                    .assetType(event.getAssetType())
                    .holdCount(event.getHoldCount())
                    .returnRate(event.getReturnRate())
                    .userNickname(event.getUserNickname())
                    .userProfileImage(event.getUserProfileImage())
                    .build();
            achievementEvaluationService.evaluateAchievements(command);
        } catch (IllegalArgumentException e) {
            log.error("[Kafka] investment.changed 역직렬화 실패: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("[Kafka] investment.changed 처리 실패: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * user.profile-updated → user_achievements의 userNickname VO 스냅샷 갱신
     */
    @KafkaListener(
            topics = "${kafka.topics.user.updated}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleUserProfileUpdated(Map<String, Object> payload) {
        try {
            UserProfileUpdatedEvent event = objectMapper.convertValue(payload, UserProfileUpdatedEvent.class);
            achievementEvaluationService.syncUserNickname(event.getUserId(), event.getNickname());
        } catch (IllegalArgumentException e) {
            log.error("[Kafka] user.profile-updated 역직렬화 실패: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("[Kafka] user.profile-updated 처리 실패: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
