package com.finlearn.achievementservice.infrastructure.persistence;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.repository.AchievementRepository;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AchievementRepositoryImpl implements AchievementRepository {

    private final AchievementJpaRepository jpaRepository;

    @Override
    public List<Achievement> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Achievement> findAllByCategory(AchievementCategory category) {
        return jpaRepository.findAllByCategory(category);
    }

    @Override
    public List<Achievement> findAllByDifficulty(AchievementDifficulty difficulty) {
        return jpaRepository.findAllByDifficulty(difficulty);
    }

    @Override
    public List<Achievement> findAllByCategoryAndDifficulty(AchievementCategory category, AchievementDifficulty difficulty) {
        return jpaRepository.findAllByCategoryAndDifficulty(category, difficulty);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public List<Achievement> saveAll(List<Achievement> achievements) {
        return jpaRepository.saveAll(achievements);
    }
}
