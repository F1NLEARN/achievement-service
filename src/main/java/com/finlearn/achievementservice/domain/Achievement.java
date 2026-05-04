package com.finlearn.achievementservice.domain;

import com.finlearn.common.domain.BaseEntity;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "achievements")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Achievement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "achievement_id", updatable = false, nullable = false)
    private UUID achievementId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private AchievementCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false, length = 20)
    private AchievementDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false, length = 30)
    private ConditionType conditionType;

    @Column(name = "condition_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal conditionValue;

    @Builder
    private Achievement(String name, AchievementCategory category, AchievementDifficulty difficulty,
                         ConditionType conditionType, BigDecimal conditionValue) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
    }

    public static Achievement create(String name, AchievementCategory category,
                                      AchievementDifficulty difficulty, ConditionType conditionType,
                                      BigDecimal conditionValue) {
        return Achievement.builder()
                .name(name)
                .category(category)
                .difficulty(difficulty)
                .conditionType(conditionType)
                .conditionValue(conditionValue)
                .build();
    }
}
