package com.finlearn.achievementservice.domain.repository;

import com.finlearn.achievementservice.domain.UserAchievement;
import java.util.List;
import java.util.UUID;

public interface UserAchievementRepository {
    List<UserAchievement> findAllByUserIdAndSeasonId(UUID userId, UUID seasonId);
    List<UserAchievement> findAllByUserId(UUID userId);
    List<UserAchievement> findAllByUserIdAndSeasonIdAndAchievementIdIn(UUID userId, UUID seasonId, List<UUID> achievementIds);
    long countByUserIdAndSeasonId(UUID userId, UUID seasonId);
    UserAchievement save(UserAchievement userAchievement);
    List<UserAchievement> saveAll(List<UserAchievement> userAchievements);
}
