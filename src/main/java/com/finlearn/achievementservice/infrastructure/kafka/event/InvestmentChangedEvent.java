package com.finlearn.achievementservice.infrastructure.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentChangedEvent {

    private UUID userId;
    private UUID seasonId;
    private Integer seasonNumber;
    private String tradeType;
    private String assetType;

    private int holdCount;
    private double returnRate;

    // VO 스냅샷 초기화용
    private String userNickname;
    private String userProfileImage;
}
