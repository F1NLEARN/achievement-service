package com.finlearn.achievementservice.infrastructure.persistence;

import com.finlearn.achievementservice.domain.UserAchievement;
import com.finlearn.achievementservice.domain.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserAchievementRepositoryImpl implements UserAchievementRepository {

    private final UserAchievementJpaRepository jpaRepository;

    @Override
    public List<UserAchievement> findAllByUserIdAndSeasonId(UUID userId, UUID seasonId) {
        return jpaRepository.findAllByUserIdAndSeasonId(userId, seasonId);
    }

    @Override
    public List<UserAchievement> findAllByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId);
    }

    @Override
    public List<UserAchievement> findAllByUserIdAndSeasonIdAndAchievementIdIn(UUID userId, UUID seasonId, List<UUID> achievementIds) {
        return jpaRepository.findAllByUserIdAndSeasonIdAndAchievementAchievementIdIn(userId, seasonId, achievementIds);
    }

    @Override
    public long countByUserIdAndSeasonId(UUID userId, UUID seasonId) {
        return jpaRepository.countByUserIdAndSeasonId(userId, seasonId);
    }

    @Override
    public UserAchievement save(UserAchievement userAchievement) {
        return jpaRepository.save(userAchievement);
    }

    @Override
    public List<UserAchievement> saveAll(List<UserAchievement> userAchievements) {
        return jpaRepository.saveAll(userAchievements);
    }
}
