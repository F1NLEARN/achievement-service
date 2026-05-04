package com.finlearn.achievementservice.domain;

import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Domain] AchievementBadge")
class AchievementBadgeTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID SEASON_ID = UUID.randomUUID();

    private Achievement achievement;
    private UserAchievement userAchievement;

    @BeforeEach
    void setUp() {
        achievement = Achievement.create(
                "ETF 마스터",
                AchievementCategory.ETF,
                AchievementDifficulty.GOLD,
                ConditionType.RETURN_RATE,
                BigDecimal.valueOf(5.0)
        );
        ReflectionTestUtils.setField(achievement, "achievementId", UUID.randomUUID());

        userAchievement = UserAchievement.achieve(
                achievement, USER_ID, "테스터", SEASON_ID, 1);
        ReflectionTestUtils.setField(userAchievement, "userAchievementId", UUID.randomUUID());
    }

    @Test
    @DisplayName("issue() 호출 시 badgeName은 achievementName + ' 뱃지'이다")
    void issue_badgeNameFormatCorrect() {
        // when
        AchievementBadge badge = AchievementBadge.issue(userAchievement);

        // then
        assertThat(badge.getBadgeName()).isEqualTo("ETF 마스터 뱃지");
    }

    @Test
    @DisplayName("issue() 호출 시 userId가 userAchievement의 userId와 동일하다")
    void issue_userIdMatchesUserAchievement() {
        // when
        AchievementBadge badge = AchievementBadge.issue(userAchievement);

        // then
        assertThat(badge.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("issue() 호출 시 paidAt이 현재 시각으로 설정된다")
    void issue_paidAtSetToNow() {
        // when
        AchievementBadge badge = AchievementBadge.issue(userAchievement);

        // then
        assertThat(badge.getPaidAt()).isNotNull();
    }

    @Test
    @DisplayName("issue() 호출 시 userAchievement 참조가 연결된다")
    void issue_userAchievementLinked() {
        // when
        AchievementBadge badge = AchievementBadge.issue(userAchievement);

        // then
        assertThat(badge.getUserAchievement()).isEqualTo(userAchievement);
    }
}
