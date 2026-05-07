package com.finlearn.achievementservice.application.strategy;

import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import org.springframework.stereotype.Component;

/**
 * RETURN_RATE 조건 판정
 * - 해당 카테고리 현재 수익률 >= conditionValue (%)
 */
@Component
public class ReturnRateStrategy implements AchievementConditionStrategy {

    @Override
    public ConditionType conditionType() {
        return ConditionType.RETURN_RATE;
    }

    @Override
    public boolean isSatisfied(Achievement achievement, EvaluateAchievementsCommand command) {
        if (!achievement.getCategory().name().equalsIgnoreCase(command.getAssetType())) {
            return false;
        }
        return command.getReturnRate() >= achievement.getConditionValue().doubleValue();
    }
}
