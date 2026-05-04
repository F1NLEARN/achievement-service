package com.finlearn.achievementservice.domain;

import com.finlearn.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "achievement_badges")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AchievementBadge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "achievement_badge_id", updatable = false, nullable = false)
    private UUID achievementBadgeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_achievement_id", nullable = false)
    private UserAchievement userAchievement;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "badge_name", nullable = false, length = 100)
    private String badgeName;

    @Column(name = "badge_image_url", length = 500)
    private String badgeImageUrl;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @Builder
    private AchievementBadge(UserAchievement userAchievement, UUID userId,
                              String badgeName, String badgeImageUrl) {
        this.userAchievement = userAchievement;
        this.userId = userId;
        this.badgeName = badgeName;
        this.badgeImageUrl = badgeImageUrl;
        this.paidAt = LocalDateTime.now();
    }

    public static AchievementBadge issue(UserAchievement userAchievement) {
        return AchievementBadge.builder()
                .userAchievement(userAchievement)
                .userId(userAchievement.getUserId())
                .badgeName(userAchievement.getAchievementName() + " 뱃지")
                .build();
    }
}
