package com.finlearn.achievementservice.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlearn.achievementservice.application.AchievementEvaluationService;
import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.infrastructure.kafka.event.InvestmentChangedEvent;
import com.finlearn.achievementservice.infrastructure.kafka.event.UserProfileUpdatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Infrastructure/Kafka] AchievementEventConsumer")
class AchievementEventConsumerTest {

    @Mock
    private AchievementEvaluationService achievementEvaluationService;

    private AchievementEventConsumer consumer;
    private ObjectMapper objectMapper;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID SEASON_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        consumer = new AchievementEventConsumer(achievementEvaluationService, objectMapper);
    }

    // ─────────────────────────────────────────────────────────────
    // handleInvestmentChanged
    // ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("handleInvestmentChanged")
    class HandleInvestmentChanged {

        @Test
        @DisplayName("이벤트의 모든 필드가 EvaluateAchievementsCommand로 올바르게 변환된다")
        void eventMappedToCommandCorrectly() {
            // given
            InvestmentChangedEvent event = new InvestmentChangedEvent(
                    USER_ID, SEASON_ID, 1, "BUY", "STOCK", 3, 5.5, "테스터", null);
            given(achievementEvaluationService.evaluateAchievements(any())).willReturn(List.of());

            // when
            consumer.handleInvestmentChanged(event);

            // then
            ArgumentCaptor<EvaluateAchievementsCommand> captor =
                    ArgumentCaptor.forClass(EvaluateAchievementsCommand.class);
            verify(achievementEvaluationService).evaluateAchievements(captor.capture());

            EvaluateAchievementsCommand command = captor.getValue();
            assertThat(command.getUserId()).isEqualTo(USER_ID);
            assertThat(command.getSeasonId()).isEqualTo(SEASON_ID);
            assertThat(command.getSeasonNumber()).isEqualTo(1);
            assertThat(command.getTradeType()).isEqualTo("BUY");
            assertThat(command.getAssetType()).isEqualTo("STOCK");
            assertThat(command.getHoldCount()).isEqualTo(3);
            assertThat(command.getReturnRate()).isEqualTo(5.5);
            assertThat(command.getUserNickname()).isEqualTo("테스터");
        }

        @Test
        @DisplayName("Map payload도 역직렬화 후 서비스를 호출한다")
        void mapPayload_deserializedAndServiceCalled() {
            // given: Kafka JsonDeserializer<Object>가 Map으로 전달하는 케이스
            Map<String, Object> payload = Map.of(
                    "userId", USER_ID.toString(),
                    "seasonId", SEASON_ID.toString(),
                    "seasonNumber", 1,
                    "tradeType", "BUY",
                    "assetType", "ETF",
                    "holdCount", 2,
                    "returnRate", 3.0,
                    "userNickname", "테스터"
            );
            given(achievementEvaluationService.evaluateAchievements(any())).willReturn(List.of());

            // when
            consumer.handleInvestmentChanged(payload);

            // then
            ArgumentCaptor<EvaluateAchievementsCommand> captor =
                    ArgumentCaptor.forClass(EvaluateAchievementsCommand.class);
            verify(achievementEvaluationService).evaluateAchievements(captor.capture());
            assertThat(captor.getValue().getAssetType()).isEqualTo("ETF");
        }

        @Test
        @DisplayName("역직렬화 실패 시 예외를 전파하지 않는다 (로그만 남김)")
        void deserializationFailure_doesNotPropagateException() {
            // given: 역직렬화 불가능한 페이로드
            Object brokenPayload = "invalid-json-payload";

            // when & then
            assertThatNoException().isThrownBy(
                    () -> consumer.handleInvestmentChanged(brokenPayload));
            verify(achievementEvaluationService, never()).evaluateAchievements(any());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // handleUserProfileUpdated
    // ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("handleUserProfileUpdated")
    class HandleUserProfileUpdated {

        @Test
        @DisplayName("이벤트의 userId와 nickname으로 syncUserNickname을 호출한다")
        void eventMappedToSyncCall() {
            // given
            UserProfileUpdatedEvent event = new UserProfileUpdatedEvent(
                    USER_ID, "신닉네임", "https://image.url");

            // when
            consumer.handleUserProfileUpdated(event);

            // then
            verify(achievementEvaluationService).syncUserNickname(USER_ID, "신닉네임");
        }

        @Test
        @DisplayName("Map payload도 역직렬화 후 서비스를 호출한다")
        void mapPayload_deserializedAndSyncCalled() {
            // given
            Map<String, Object> payload = Map.of(
                    "userId", USER_ID.toString(),
                    "nickname", "신닉네임",
                    "profileImage", "https://image.url"
            );

            // when
            consumer.handleUserProfileUpdated(payload);

            // then
            verify(achievementEvaluationService).syncUserNickname(USER_ID, "신닉네임");
        }

        @Test
        @DisplayName("역직렬화 실패 시 예외를 전파하지 않는다 (로그만 남김)")
        void deserializationFailure_doesNotPropagateException() {
            // given
            Object brokenPayload = "invalid";

            // when & then
            assertThatNoException().isThrownBy(
                    () -> consumer.handleUserProfileUpdated(brokenPayload));
            verify(achievementEvaluationService, never()).syncUserNickname(any(), any());
        }
    }
}
