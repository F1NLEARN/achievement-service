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

@DisplayName("[Application/Strategy] ReturnRateStrategy")
class ReturnRateStrategyTest {

    private ReturnRateStrategy strategy;

    /** 수익률 5.0% 이상 조건 */
    private Achievement returnRateAchievement;

    @BeforeEach
    void setUp() {
        strategy = new ReturnRateStrategy();

        returnRateAchievement = Achievement.create(
                "코스피 정복자", AchievementCategory.STOCK,
                AchievementDifficulty.GOLD, ConditionType.RETURN_RATE, BigDecimal.valueOf(5.0));
    }

    @Test
    @DisplayName("conditionType()은 RETURN_RATE를 반환한다")
    void conditionType_returnsReturnRate() {
        assertThat(strategy.conditionType()).isEqualTo(ConditionType.RETURN_RATE);
    }

    @Test
    @DisplayName("returnRate >= conditionValue → 조건 충족")
    void isSatisfied_returnRateMeetsCondition_true() {
        // given: returnRate=5.0, conditionValue=5.0
        EvaluateAchievementsCommand command = command("STOCK", 5.0);

        // when & then
        assertThat(strategy.isSatisfied(returnRateAchievement, command)).isTrue();
    }

    @Test
    @DisplayName("returnRate > conditionValue → 조건 충족")
    void isSatisfied_returnRateExceedsCondition_true() {
        // given: returnRate=12.5, conditionValue=5.0
        EvaluateAchievementsCommand command = command("STOCK", 12.5);

        // when & then
        assertThat(strategy.isSatisfied(returnRateAchievement, command)).isTrue();
    }

    @Test
    @DisplayName("returnRate < conditionValue → 조건 미충족")
    void isSatisfied_returnRateBelowCondition_false() {
        // given: returnRate=4.9, conditionValue=5.0
        EvaluateAchievementsCommand command = command("STOCK", 4.9);

        // when & then
        assertThat(strategy.isSatisfied(returnRateAchievement, command)).isFalse();
    }

    @Test
    @DisplayName("assetType과 업적 카테고리 불일치 → 조건 미충족")
    void isSatisfied_categoryMismatch_false() {
        // given: ETF 이벤트인데 STOCK 업적 판정
        EvaluateAchievementsCommand command = command("ETF", 10.0);

        // when & then
        assertThat(strategy.isSatisfied(returnRateAchievement, command)).isFalse();
    }

    private EvaluateAchievementsCommand command(String assetType, double returnRate) {
        return EvaluateAchievementsCommand.builder()
                .userId(UUID.randomUUID())
                .seasonId(UUID.randomUUID())
                .seasonNumber(1)
                .tradeType("BUY")
                .assetType(assetType)
                .holdCount(1)
                .returnRate(returnRate)
                .userNickname("테스터")
                .build();
    }
}
