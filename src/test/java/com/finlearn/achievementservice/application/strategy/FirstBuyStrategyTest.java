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

@DisplayName("[Application/Strategy] FirstBuyStrategy")
class FirstBuyStrategyTest {

    private FirstBuyStrategy strategy;
    private Achievement stockAchievement;
    private Achievement etfAchievement;

    @BeforeEach
    void setUp() {
        strategy = new FirstBuyStrategy();

        stockAchievement = Achievement.create(
                "국내 입문자", AchievementCategory.STOCK,
                AchievementDifficulty.BRONZE, ConditionType.FIRST_BUY, BigDecimal.ONE);

        etfAchievement = Achievement.create(
                "ETF 첫걸음", AchievementCategory.ETF,
                AchievementDifficulty.BRONZE, ConditionType.FIRST_BUY, BigDecimal.ONE);
    }

    @Test
    @DisplayName("conditionType()은 FIRST_BUY를 반환한다")
    void conditionType_returnsFirstBuy() {
        assertThat(strategy.conditionType()).isEqualTo(ConditionType.FIRST_BUY);
    }

    @Test
    @DisplayName("tradeType=BUY, assetType=STOCK, 업적 카테고리=STOCK → 조건 충족")
    void isSatisfied_buyStock_stockAchievement_true() {
        // given
        EvaluateAchievementsCommand command = command("BUY", "STOCK");

        // when & then
        assertThat(strategy.isSatisfied(stockAchievement, command)).isTrue();
    }

    @Test
    @DisplayName("tradeType=BUY, assetType=ETF, 업적 카테고리=ETF → 조건 충족")
    void isSatisfied_buyEtf_etfAchievement_true() {
        // given
        EvaluateAchievementsCommand command = command("BUY", "ETF");

        // when & then
        assertThat(strategy.isSatisfied(etfAchievement, command)).isTrue();
    }

    @Test
    @DisplayName("tradeType=SELL → 조건 미충족")
    void isSatisfied_sell_false() {
        // given
        EvaluateAchievementsCommand command = command("SELL", "STOCK");

        // when & then
        assertThat(strategy.isSatisfied(stockAchievement, command)).isFalse();
    }

    @Test
    @DisplayName("assetType과 업적 카테고리 불일치 → 조건 미충족")
    void isSatisfied_categoryMismatch_false() {
        // given: STOCK 이벤트인데 ETF 업적 판정
        EvaluateAchievementsCommand command = command("BUY", "STOCK");

        // when & then
        assertThat(strategy.isSatisfied(etfAchievement, command)).isFalse();
    }

    private EvaluateAchievementsCommand command(String tradeType, String assetType) {
        return EvaluateAchievementsCommand.builder()
                .userId(UUID.randomUUID())
                .seasonId(UUID.randomUUID())
                .seasonNumber(1)
                .tradeType(tradeType)
                .assetType(assetType)
                .holdCount(1)
                .returnRate(0.0)
                .userNickname("테스터")
                .build();
    }
}
