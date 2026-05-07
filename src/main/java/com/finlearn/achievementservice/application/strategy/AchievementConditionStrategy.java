package com.finlearn.achievementservice.application.strategy;

import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.ConditionType;

/**
 * 업적 조건 판정 전략 인터페이스
 * 새로운 조건 타입 추가 시 이 인터페이스를 구현하는 클래스만 추가하면 됨
 */
public interface AchievementConditionStrategy {

    ConditionType conditionType();

    boolean isSatisfied(Achievement achievement, EvaluateAchievementsCommand command);
}
