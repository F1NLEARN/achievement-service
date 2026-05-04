package com.finlearn.achievementservice.application;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.repository.AchievementRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Application] AchievementService")
class AchievementServiceTest {

    @Mock private AchievementRepository achievementRepository;

    private AchievementService sut;

    @BeforeEach
    void setUp() {
        sut = new AchievementService(achievementRepository);
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
