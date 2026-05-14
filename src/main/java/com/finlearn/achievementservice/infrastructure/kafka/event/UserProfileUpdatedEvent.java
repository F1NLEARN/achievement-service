package com.finlearn.achievementservice.infrastructure.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

/** 유저 서비스 발행 → user_achievements의 user_nickname VO 스냅샷 갱신
 *
 * topic: user.updated */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdatedEvent {
    private UUID userId;
    private String nickname;
    private String profileImage;
}
