package com.finlearn.achievementservice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finlearn.achievementservice.application.AchievementService;
import com.finlearn.achievementservice.domain.Achievement;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Presentation] AchievementController")
class AchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    private MockMvc mockMvc;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID SEASON_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AchievementController(achievementService))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/v1/achievements
    // ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /api/v1/achievements")
    class GetAllAchievements {

        @Test
        @DisplayName("필터 없이 호출 시 200 OK, achievements 배열 반환")
        void noFilter_returns200WithAchievementsList() throws Exception {
            // given
            Achievement achievement = createAchievement(AchievementCategory.STOCK,
                    AchievementDifficulty.BRONZE, ConditionType.FIRST_BUY, BigDecimal.ONE);
            given(achievementService.getAllAchievements(null, null))
                    .willReturn(List.of(achievement));

            // when & then
            mockMvc.perform(get("/api/v1/achievements"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievements").isArray())
                    .andExpect(jsonPath("$.achievements.length()").value(1))
                    .andExpect(jsonPath("$.achievements[0].name").value("테스트업적"));
        }

        @Test
        @DisplayName("category=STOCK 필터 호출 시 서비스에 STOCK 카테고리 전달")
        void categoryFilter_passesCorrectCategory() throws Exception {
            // given
            given(achievementService.getAllAchievements(eq(AchievementCategory.STOCK), any()))
                    .willReturn(List.of());

            // when & then
            mockMvc.perform(get("/api/v1/achievements")
                            .param("category", "STOCK"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievements").isArray());
        }

        @Test
        @DisplayName("difficulty=GOLD 필터 호출 시 서비스에 GOLD 난이도 전달")
        void difficultyFilter_passesCorrectDifficulty() throws Exception {
            // given
            given(achievementService.getAllAchievements(any(), eq(AchievementDifficulty.GOLD)))
                    .willReturn(List.of());

            // when & then
            mockMvc.perform(get("/api/v1/achievements")
                            .param("difficulty", "GOLD"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievements").isArray());
        }

        @Test
        @DisplayName("category + difficulty 조합 필터 호출 시 200 OK 반환")
        void bothFilters_returns200() throws Exception {
            // given
            given(achievementService.getAllAchievements(
                    AchievementCategory.ETF, AchievementDifficulty.SILVER))
                    .willReturn(List.of());

            // when & then
            mockMvc.perform(get("/api/v1/achievements")
                            .param("category", "ETF")
                            .param("difficulty", "SILVER"))
                    .andExpect(status().isOk());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 테스트 헬퍼
    // ─────────────────────────────────────────────────────────────

    private Achievement createAchievement(AchievementCategory category,
                                           AchievementDifficulty difficulty,
                                           ConditionType conditionType,
                                           BigDecimal conditionValue) {
        Achievement a = Achievement.create("테스트업적", category, difficulty, conditionType, conditionValue);
        ReflectionTestUtils.setField(a, "achievementId", UUID.randomUUID());
        return a;
    }
}
