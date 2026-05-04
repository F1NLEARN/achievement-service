package com.finlearn.achievementservice.domain;

import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Domain] Achievement")
class AchievementTest {

    @Test
    @DisplayName("create() 호출 시 모든 필드가 정상 설정된다")
    void create_allFieldsSet() {
        // given
        String name = "ETF 마스터";
        AchievementCategory category = AchievementCategory.ETF;
        AchievementDifficulty difficulty = AchievementDifficulty.GOLD;
        ConditionType conditionType = ConditionType.RETURN_RATE;
        BigDecimal conditionValue = BigDecimal.valueOf(5.0);

        // when
        Achievement achievement = Achievement.create(name, category, difficulty, conditionType, conditionValue);

        // then
        assertThat(achievement.getName()).isEqualTo(name);
        assertThat(achievement.getCategory()).isEqualTo(category);
        assertThat(achievement.getDifficulty()).isEqualTo(difficulty);
        assertThat(achievement.getConditionType()).isEqualTo(conditionType);
        assertThat(achievement.getConditionValue()).isEqualByComparingTo(conditionValue);
    }

    @Test
    @DisplayName("DB 저장 전 achievementId는 null이다")
    void create_achievementIdIsNullBeforePersist() {
        // when
        Achievement achievement = Achievement.create(
                "국내 입문자",
                AchievementCategory.STOCK,
                AchievementDifficulty.BRONZE,
                ConditionType.FIRST_BUY,
                BigDecimal.ONE
        );

        // then
        assertThat(achievement.getAchievementId()).isNull();
    }


}
