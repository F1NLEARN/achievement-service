package com.finlearn.achievementservice.application;

import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.repository.AchievementRepository;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.AchievementDifficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {

    private final AchievementRepository achievementRepository;

    // 전체 업적 목록 조회
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
}
