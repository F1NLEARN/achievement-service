package com.finlearn.achievementservice.application.command;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class EvaluateAchievementsCommand {
    private UUID userId;
    private UUID seasonId;
    private Integer seasonNumber;
    private String tradeType;
    private String assetType;
    private int holdCount;
    private double returnRate;
    private String userNickname;
    private String userProfileImage;
}
