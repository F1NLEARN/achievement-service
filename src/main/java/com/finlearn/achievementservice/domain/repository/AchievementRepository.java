package com.finlearn.achievementservice.domain.repository;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import java.util.List;

public interface AchievementRepository {
    List<Achievement> findAll();
    List<Achievement> findAllByCategory(AchievementCategory category);
    List<Achievement> findAllByDifficulty(AchievementDifficulty difficulty);
    List<Achievement> findAllByCategoryAndDifficulty(AchievementCategory category, AchievementDifficulty difficulty);
    boolean existsByName(String name);
    long count();
    List<Achievement> saveAll(List<Achievement> achievements);
}
