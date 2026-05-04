package com.finlearn.achievementservice.infrastructure.persistence;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AchievementJpaRepository extends JpaRepository<Achievement, UUID> {

    List<Achievement> findAllByCategory(AchievementCategory category);

    List<Achievement> findAllByDifficulty(AchievementDifficulty difficulty);

    List<Achievement> findAllByCategoryAndDifficulty(AchievementCategory category, AchievementDifficulty difficulty);

    boolean existsByName(String name);
}
