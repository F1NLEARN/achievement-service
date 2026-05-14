package com.finlearn.achievementservice.application.strategy;

import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import org.springframework.stereotype.Component;

/**
 * FIRST_BUY 조건 판정
 * - tradeType == BUY
 * - assetType == 업적 카테고리
 * 이미 달성한 업적은 판정 대상에서 제외되므로, 조건 충족 = 해당 카테고리 첫 매수
 */
@Component
public class FirstBuyStrategy implements AchievementConditionStrategy {

    @Override
    public ConditionType conditionType() {
        return ConditionType.FIRST_BUY;
    }

    @Override
    public boolean isSatisfied(Achievement achievement, EvaluateAchievementsCommand command) {
        return "BUY".equalsIgnoreCase(command.getTradeType())
                && achievement.getCategory().name().equalsIgnoreCase(command.getAssetType());
    }
}
