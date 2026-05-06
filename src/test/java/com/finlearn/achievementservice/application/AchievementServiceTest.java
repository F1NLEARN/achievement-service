package com.finlearn.achievementservice.application;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.repository.AchievementRepository;
import com.finlearn.achievementservice.domain.repository.UserAchievementRepository;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] AchievementService")
class AchievementServiceTest {

    @Mock private AchievementRepository achievementRepository;
    @Mock private UserAchievementRepository userAchievementRepository;

    private AchievementService sut;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID SEASON_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        sut = new AchievementService(achievementRepository, userAchievementRepository);
    }

    // ─────────────────────────────────────────────────────────────
    // 모든 업적 조회
    // ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getAllAchievements")
    class GetAllAchievements {

        @Test
        @DisplayName("필터 없음 → findAll() 호출")
        void noFilter_callsFindAll() {
            given(achievementRepository.findAll()).willReturn(List.of());

            sut.getAllAchievements(null, null);

            verify(achievementRepository).findAll();
        }

        @Test
        @DisplayName("category 단독 필터 → findAllByCategory() 호출")
        void categoryOnly_callsFindAllByCategory() {
            given(achievementRepository.findAllByCategory(AchievementCategory.STOCK))
                    .willReturn(List.of());

            sut.getAllAchievements(AchievementCategory.STOCK, null);

            verify(achievementRepository).findAllByCategory(AchievementCategory.STOCK);
        }

        @Test
        @DisplayName("difficulty 단독 필터 → findAllByDifficulty() 호출")
        void difficultyOnly_callsFindAllByDifficulty() {
            given(achievementRepository.findAllByDifficulty(AchievementDifficulty.GOLD))
                    .willReturn(List.of());

            sut.getAllAchievements(null, AchievementDifficulty.GOLD);

            verify(achievementRepository).findAllByDifficulty(AchievementDifficulty.GOLD);
        }

        @Test
        @DisplayName("category + difficulty 조합 필터 → findAllByCategoryAndDifficulty() 호출")
        void bothFilters_callsFindAllByCategoryAndDifficulty() {
            given(achievementRepository.findAllByCategoryAndDifficulty(
                    AchievementCategory.ETF, AchievementDifficulty.SILVER))
                    .willReturn(List.of());

            sut.getAllAchievements(AchievementCategory.ETF, AchievementDifficulty.SILVER);

            verify(achievementRepository).findAllByCategoryAndDifficulty(
                    AchievementCategory.ETF, AchievementDifficulty.SILVER);
        }

        @Test
        @DisplayName("조회된 Achievement 목록을 그대로 반환한다")
        void returnsAchievementList() {
            Achievement achievement = createAchievement(
                    AchievementCategory.STOCK, ConditionType.FIRST_BUY, BigDecimal.ONE);
            given(achievementRepository.findAll()).willReturn(List.of(achievement));

            List<Achievement> result = sut.getAllAchievements(null, null);

            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(achievement);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 내 업적 조회
    // ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getMyAchievements")
    class GetMyAchievements {

        @Test
        @DisplayName("seasonId 있음 → findAllByUserIdAndSeasonId() 호출")
        void withSeasonId_callsSeasonFiltered() {
            given(userAchievementRepository.findAllByUserIdAndSeasonId(USER_ID, SEASON_ID))
                    .willReturn(List.of());

            sut.getMyAchievements(USER_ID, SEASON_ID);

            verify(userAchievementRepository).findAllByUserIdAndSeasonId(USER_ID, SEASON_ID);
            verify(userAchievementRepository, never()).findAllByUserId(any());
        }

        @Test
        @DisplayName("seasonId 없음 → findAllByUserId() 호출")
        void withoutSeasonId_callsUserFiltered() {
            given(userAchievementRepository.findAllByUserId(USER_ID)).willReturn(List.of());

            sut.getMyAchievements(USER_ID, null);

            verify(userAchievementRepository).findAllByUserId(USER_ID);
            verify(userAchievementRepository, never())
                    .findAllByUserIdAndSeasonId(any(), any());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 시즌 내 달성 업적 조회
    // ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getMyAchievementCount")
    class GetMyAchievementCount {

        @Test
        @DisplayName("시즌 내 달성 업적 수를 반환한다")
        void returnsCount() {
            given(userAchievementRepository.countByUserIdAndSeasonId(USER_ID, SEASON_ID))
                    .willReturn(7L);

            long count = sut.getMyAchievementCount(USER_ID, SEASON_ID);

            assertThat(count).isEqualTo(7L);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 테스트 헬퍼
    // ─────────────────────────────────────────────────────────────

    private Achievement createAchievement(AchievementCategory category,
                                           ConditionType conditionType,
                                           BigDecimal conditionValue) {
        Achievement achievement = Achievement.create(
                "테스트업적", category, AchievementDifficulty.BRONZE, conditionType, conditionValue);
        ReflectionTestUtils.setField(achievement, "achievementId", UUID.randomUUID());
        return achievement;
    }
}
