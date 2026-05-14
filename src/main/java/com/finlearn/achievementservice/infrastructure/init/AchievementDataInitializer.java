package com.finlearn.achievementservice.infrastructure.init;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import com.finlearn.achievementservice.domain.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * 서비스 시작 시 사전 정의된 업적 마스터 데이터 초기화
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementDataInitializer implements ApplicationRunner {

    private final AchievementRepository achievementRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (achievementRepository.count() > 0) {
            log.info("[AchievementInit] 업적 마스터 데이터 이미 존재. 초기화 건너뜀.");
            return;
        }

        List<Achievement> achievements = List.of(
                Achievement.create(
                        "국내 입문자",
                        AchievementCategory.STOCK,
                        AchievementDifficulty.BRONZE,
                        ConditionType.FIRST_BUY,
                        BigDecimal.ONE
                ),
                Achievement.create(
                        "배당 수집가",
                        AchievementCategory.STOCK,
                        AchievementDifficulty.SILVER,
                        ConditionType.HOLD_COUNT,
                        BigDecimal.valueOf(3)
                ),
                Achievement.create(
                        "코스피 정복자",
                        AchievementCategory.STOCK,
                        AchievementDifficulty.GOLD,
                        ConditionType.RETURN_RATE,
                        BigDecimal.valueOf(5.0)
                ),
                Achievement.create(
                        "ETF 첫걸음",
                        AchievementCategory.ETF,
                        AchievementDifficulty.BRONZE,
                        ConditionType.FIRST_BUY,
                        BigDecimal.ONE
                ),
                Achievement.create(
                        "분산의 달인",
                        AchievementCategory.ETF,
                        AchievementDifficulty.SILVER,
                        ConditionType.HOLD_COUNT,
                        BigDecimal.valueOf(3)
                ),
                Achievement.create(
                        "ETF 마스터",
                        AchievementCategory.ETF,
                        AchievementDifficulty.GOLD,
                        ConditionType.RETURN_RATE,
                        BigDecimal.valueOf(5.0)
                )
        );

        achievementRepository.saveAll(achievements);
        log.info("[AchievementInit] 업적 마스터 데이터 {}개 초기화 완료", achievements.size());
    }
}
