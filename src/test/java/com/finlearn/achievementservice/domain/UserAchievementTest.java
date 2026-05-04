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

@DisplayName("[Domain] UserAchievement")
class UserAchievementTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID SEASON_ID = UUID.randomUUID();

    private Achievement achievement;

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
    }

    @Test
    @DisplayName("achieve() 호출 시 업적명/카테고리/난이도 VO 스냅샷이 초기화된다")
    void achieve_voSnapshotInitialized() {
        // when
        UserAchievement userAchievement = UserAchievement.achieve(
                achievement, USER_ID, "테스터", SEASON_ID, 1);

        // then
        assertThat(userAchievement.getAchievementName()).isEqualTo("ETF 마스터");
        assertThat(userAchievement.getAchievementCategory()).isEqualTo(AchievementCategory.ETF);
        assertThat(userAchievement.getAchievementDifficulty()).isEqualTo(AchievementDifficulty.GOLD);
    }

    @Test
    @DisplayName("achieve() 호출 시 userId, seasonId, seasonNumber가 정상 설정된다")
    void achieve_identifierFieldsSet() {
        // when
        UserAchievement userAchievement = UserAchievement.achieve(
                achievement, USER_ID, "테스터", SEASON_ID, 2);

        // then
        assertThat(userAchievement.getUserId()).isEqualTo(USER_ID);
        assertThat(userAchievement.getSeasonId()).isEqualTo(SEASON_ID);
        assertThat(userAchievement.getSeasonNumber()).isEqualTo(2);
    }

    @Test
    @DisplayName("achieve() 호출 시 achievedAt이 현재 시각으로 설정된다")
    void achieve_achievedAtSetToNow() {
        // when
        UserAchievement userAchievement = UserAchievement.achieve(
                achievement, USER_ID, "테스터", SEASON_ID, 1);

        // then
        assertThat(userAchievement.getAchievedAt()).isNotNull();
    }

    @Test
    @DisplayName("updateNickname() 호출 시 닉네임 VO 스냅샷이 갱신된다")
    void updateNickname_snapshotUpdated() {
        // given
        UserAchievement userAchievement = UserAchievement.achieve(
                achievement, USER_ID, "구닉네임", SEASON_ID, 1);

        // when
        userAchievement.updateNickname("신닉네임");

        // then
        assertThat(userAchievement.getUserNickname()).isEqualTo("신닉네임");
    }


}
