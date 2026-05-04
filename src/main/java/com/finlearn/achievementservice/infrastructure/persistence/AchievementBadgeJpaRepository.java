package com.finlearn.achievementservice.infrastructure.persistence;

import com.finlearn.achievementservice.domain.AchievementBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AchievementBadgeJpaRepository extends JpaRepository<AchievementBadge, UUID> {
}
