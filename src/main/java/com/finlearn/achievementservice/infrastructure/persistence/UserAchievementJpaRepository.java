package com.finlearn.achievementservice.infrastructure.persistence;

import com.finlearn.achievementservice.domain.UserAchievement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserAchievementJpaRepository extends JpaRepository<UserAchievement, UUID> {

    // 특정 시즌에서 유저가 달성한 업적 목록
    List<UserAchievement> findAllByUserIdAndSeasonId(UUID userId, UUID seasonId);

    // 전체 달성 목록
    List<UserAchievement> findAllByUserId(UUID userId);

    // 이미 달성한 업적 ID 목록 조회
    @EntityGraph(attributePaths = "achievement")
    List<UserAchievement> findAllByUserIdAndSeasonIdAndAchievementAchievementIdIn(
            UUID userId, UUID seasonId, List<UUID> achievementIds);

    // 특정 시즌 업적 수: 시즌 서비스 내부 API 용
    long countByUserIdAndSeasonId(UUID userId, UUID seasonId);
}
