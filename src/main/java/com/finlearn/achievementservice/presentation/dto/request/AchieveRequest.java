package com.finlearn.achievementservice.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class AchieveRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID seasonId;

    @NotNull
    private Integer seasonNumber;

    @NotBlank
    private String tradeType;

    @NotBlank
    private String assetType;

    private int holdCount;

    private double returnRate;

    private String userNickname;
}
