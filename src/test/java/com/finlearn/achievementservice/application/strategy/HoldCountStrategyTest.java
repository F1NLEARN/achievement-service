package com.finlearn.achievementservice.application.strategy;

import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Application/Strategy] HoldCountStrategy")
class HoldCountStrategyTest {

    private HoldCountStrategy strategy;

    /** 보유 종목 3개 이상 조건 */
    private Achievement holdCountAchievement;

    @BeforeEach
    void setUp() {
        strategy = new HoldCountStrategy();

        holdCountAchievement = Achievement.create(
                "배당 수집가", AchievementCategory.STOCK,
                AchievementDifficulty.SILVER, ConditionType.HOLD_COUNT, BigDecimal.valueOf(3));
    }

    @Test
    @DisplayName("conditionType()은 HOLD_COUNT를 반환한다")
    void conditionType_returnsHoldCount() {
        assertThat(strategy.conditionType()).isEqualTo(ConditionType.HOLD_COUNT);
    }

    @Test
    @DisplayName("holdCount >= conditionValue → 조건 충족")
    void isSatisfied_holdCountMeetsCondition_true() {
        // given: holdCount=3, conditionValue=3
        EvaluateAchievementsCommand command = command("STOCK", 3);

        // when & then
        assertThat(strategy.isSatisfied(holdCountAchievement, command)).isTrue();
    }

    @Test
    @DisplayName("holdCount > conditionValue → 조건 충족")
    void isSatisfied_holdCountExceedsCondition_true() {
        // given: holdCount=5, conditionValue=3
        EvaluateAchievementsCommand command = command("STOCK", 5);

        // when & then
        assertThat(strategy.isSatisfied(holdCountAchievement, command)).isTrue();
    }

    @Test
    @DisplayName("holdCount < conditionValue → 조건 미충족")
    void isSatisfied_holdCountBelowCondition_false() {
        // given: holdCount=2, conditionValue=3
        EvaluateAchievementsCommand command = command("STOCK", 2);

        // when & then
        assertThat(strategy.isSatisfied(holdCountAchievement, command)).isFalse();
    }

    @Test
    @DisplayName("assetType과 업적 카테고리 불일치 → 조건 미충족")
    void isSatisfied_categoryMismatch_false() {
        // given: ETF 이벤트인데 STOCK 업적 판정
        EvaluateAchievementsCommand command = command("ETF", 5);

        // when & then
        assertThat(strategy.isSatisfied(holdCountAchievement, command)).isFalse();
    }

    private EvaluateAchievementsCommand command(String assetType, int holdCount) {
        return EvaluateAchievementsCommand.builder()
                .userId(UUID.randomUUID())
                .seasonId(UUID.randomUUID())
                .seasonNumber(1)
                .tradeType("BUY")
                .assetType(assetType)
                .holdCount(holdCount)
                .returnRate(0.0)
                .userNickname("테스터")
                .build();
    }
}
