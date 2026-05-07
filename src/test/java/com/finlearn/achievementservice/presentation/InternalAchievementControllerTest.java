package com.finlearn.achievementservice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finlearn.achievementservice.application.AchievementEvaluationService;
import com.finlearn.achievementservice.application.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.UUID;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Presentation] InternalAchievementController")
class InternalAchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementEvaluationService achievementEvaluationService;

    private MockMvc mockMvc;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID SEASON_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(new InternalAchievementController(achievementService, achievementEvaluationService))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    // ─────────────────────────────────────────────────────────────
    // GET /internal/v1/achievements/count
    // ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /internal/v1/achievements/count")
    class GetAchievementCount {

        @Test
        @DisplayName("userId와 seasonId로 호출 시 200 OK, achievementCount 반환")
        void returns200WithCount() throws Exception {
            // given
            given(achievementService.getMyAchievementCount(USER_ID, SEASON_ID)).willReturn(3L);

            // when & then
            mockMvc.perform(get("/internal/v1/achievements/count")
                            .param("userId", USER_ID.toString())
                            .param("seasonId", SEASON_ID.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(USER_ID.toString()))
                    .andExpect(jsonPath("$.seasonId").value(SEASON_ID.toString()))
                    .andExpect(jsonPath("$.achievementCount").value(3));
        }

        @Test
        @DisplayName("userId 파라미터 누락 시 400 Bad Request 반환")
        void missingUserId_returns400() throws Exception {
            mockMvc.perform(get("/internal/v1/achievements/count")
                            .param("seasonId", SEASON_ID.toString()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("seasonId 파라미터 누락 시 400 Bad Request 반환")
        void missingSeasonId_returns400() throws Exception {
            mockMvc.perform(get("/internal/v1/achievements/count")
                            .param("userId", USER_ID.toString()))
                    .andExpect(status().isBadRequest());
        }
    }
}
