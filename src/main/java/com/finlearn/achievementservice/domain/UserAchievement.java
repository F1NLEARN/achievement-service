package com.finlearn.achievementservice.domain;

import com.finlearn.common.domain.BaseEntity;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(
        name = "user_achievements",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_achievements_user_achievement_season",
                columnNames = {"user_id", "achievement_id", "season_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAchievement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_achievement_id", updatable = false, nullable = false)
    private UUID userAchievementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    // ────────────────────────── VO 스냅샷:  ─────────────────────────
    @Column(name = "achievement_name", nullable = false, length = 100)
    private String achievementName;

    @Enumerated(EnumType.STRING)
    @Column(name = "achievement_category", nullable = false, length = 20)
    private AchievementCategory achievementCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "achievement_difficulty", nullable = false, length = 20)
    private AchievementDifficulty achievementDifficulty;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "user_nickname", nullable = false, length = 50)
    private String userNickname;

    @Column(name = "season_id", nullable = false)
    private UUID seasonId;

    @Column(name = "season_number", nullable = false)
    private Integer seasonNumber;
    // ───────────────────────────────────────────────────────────────

    @Column(name = "achieved_at", nullable = false)
    private LocalDateTime achievedAt;

    @Builder
    private UserAchievement(Achievement achievement, UUID userId, String userNickname,
                             UUID seasonId, Integer seasonNumber) {
        this.achievement = achievement;
        this.achievementName = achievement.getName();
        this.achievementCategory = achievement.getCategory();
        this.achievementDifficulty = achievement.getDifficulty();
        this.userId = userId;
        this.userNickname = userNickname;
        this.seasonId = seasonId;
        this.seasonNumber = seasonNumber;
        this.achievedAt = LocalDateTime.now();
    }

    public static UserAchievement achieve(Achievement achievement, UUID userId, String userNickname,
                                           UUID seasonId, Integer seasonNumber) {
        return UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .userNickname(userNickname)
                .seasonId(seasonId)
                .seasonNumber(seasonNumber)
                .build();
    }

    // UserProfileUpdated 이벤트 수신 시 닉네임 VO 스냅샷 갱신
    public void updateNickname(String newNickname) {
        this.userNickname = newNickname;
    }
}
