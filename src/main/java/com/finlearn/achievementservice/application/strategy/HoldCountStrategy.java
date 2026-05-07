package com.finlearn.achievementservice.application.strategy;

import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import org.springframework.stereotype.Component;

/**
 * HOLD_COUNT 조건 판정
 * - 해당 카테고리 현재 보유 종목 수 >= conditionValue
 */
@Component
public class HoldCountStrategy implements AchievementConditionStrategy {

    @Override
    public ConditionType conditionType() {
        return ConditionType.HOLD_COUNT;
    }

    @Override
    public boolean isSatisfied(Achievement achievement, EvaluateAchievementsCommand command) {
        if (!achievement.getCategory().name().equalsIgnoreCase(command.getAssetType())) {
            return false;
        }
        return command.getHoldCount() >= achievement.getConditionValue().intValue();
    }
}
