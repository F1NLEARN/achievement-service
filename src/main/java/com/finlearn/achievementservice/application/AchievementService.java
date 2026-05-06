package com.finlearn.achievementservice.application;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.UserAchievement;
import com.finlearn.achievementservice.domain.repository.AchievementRepository;
import com.finlearn.achievementservice.domain.repository.UserAchievementRepository;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;

    public List<Achievement> getAllAchievements(AchievementCategory category,
                                                AchievementDifficulty difficulty) {
        if (category != null && difficulty != null) {
            return achievementRepository.findAllByCategoryAndDifficulty(category, difficulty);
        } else if (category != null) {
            return achievementRepository.findAllByCategory(category);
        } else if (difficulty != null) {
            return achievementRepository.findAllByDifficulty(difficulty);
        }
        return achievementRepository.findAll();
    }

    public List<UserAchievement> getMyAchievements(UUID userId, UUID seasonId) {
        if (seasonId != null) {
            return userAchievementRepository.findAllByUserIdAndSeasonId(userId, seasonId);
        }
        return userAchievementRepository.findAllByUserId(userId);
    }

    public long getMyAchievementCount(UUID userId, UUID seasonId) {
        return userAchievementRepository.countByUserIdAndSeasonId(userId, seasonId);
    }
}
