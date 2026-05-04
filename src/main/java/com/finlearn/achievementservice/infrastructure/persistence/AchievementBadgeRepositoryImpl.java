package com.finlearn.achievementservice.infrastructure.persistence;

import com.finlearn.achievementservice.domain.AchievementBadge;
import com.finlearn.achievementservice.domain.repository.AchievementBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AchievementBadgeRepositoryImpl implements AchievementBadgeRepository {

    private final AchievementBadgeJpaRepository jpaRepository;

    @Override
    public AchievementBadge save(AchievementBadge badge) {
        return jpaRepository.save(badge);
    }
}
