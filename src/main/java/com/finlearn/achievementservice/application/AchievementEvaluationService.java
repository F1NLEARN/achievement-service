package com.finlearn.achievementservice.application;

import com.finlearn.achievementservice.application.command.EvaluateAchievementsCommand;
import com.finlearn.achievementservice.application.port.AchievementEventPublisher;
import com.finlearn.achievementservice.application.strategy.AchievementConditionStrategy;
import com.finlearn.achievementservice.domain.Achievement;
import com.finlearn.achievementservice.domain.AchievementBadge;
import com.finlearn.achievementservice.domain.UserAchievement;
import com.finlearn.achievementservice.domain.repository.AchievementBadgeRepository;
import com.finlearn.achievementservice.domain.repository.AchievementRepository;
import com.finlearn.achievementservice.domain.repository.UserAchievementRepository;
import com.finlearn.achievementservice.domain.vo.AchievementCategory;
import com.finlearn.achievementservice.domain.vo.ConditionType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 업적 달성 판정 및 닉네임 동기화 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementEvaluationService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementBadgeRepository achievementBadgeRepository;
    private final AchievementEventPublisher eventPublisher;
    private final List<AchievementConditionStrategy> strategies;

    // ConditionType → Strategy (생성자 주입 후 캐싱)
    private Map<ConditionType, AchievementConditionStrategy> strategyMap;

    @PostConstruct
    void initStrategyMap() {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        AchievementConditionStrategy::conditionType,
                        Function.identity()
                ));
    }

    /**
     * InvestmentChanged 이벤트 기반 업적 조건 판정
     * 달성된 업적 목록 반환
     */
    @Transactional
    public List<UserAchievement> evaluateAchievements(EvaluateAchievementsCommand command) {
        UUID userId = command.getUserId();
        UUID seasonId = command.getSeasonId();
        AchievementCategory category = AchievementCategory.valueOf(command.getAssetType().toUpperCase());

        List<Achievement> allCategoryAchievements = achievementRepository.findAllByCategory(category);
        if (allCategoryAchievements.isEmpty()) return List.of();

        Set<UUID> alreadyAchievedIds = new HashSet<>(
                userAchievementRepository
                        .findAllByUserIdAndSeasonIdAndAchievementIdIn(
                                userId, seasonId,
                                allCategoryAchievements.stream()
                                        .map(Achievement::getAchievementId)
                                        .toList())
                        .stream()
                        .map(ua -> ua.getAchievement().getAchievementId())
                        .toList());

        List<UserAchievement> newlyAchieved = new ArrayList<>();
        allCategoryAchievements.stream()
                .filter(a -> !alreadyAchievedIds.contains(a.getAchievementId()))
                .forEach(achievement -> evaluateSingle(achievement, command)
                        .ifPresent(newlyAchieved::add));

        return newlyAchieved;
    }

    /** UserProfileUpdated 이벤트 수신 시 닉네임 VO 스냅샷 갱신 */
    @Transactional
    public void syncUserNickname(UUID userId, String newNickname) {
        List<UserAchievement> userAchievements = userAchievementRepository.findAllByUserId(userId);
        userAchievements.forEach(ua -> ua.updateNickname(newNickname));
        userAchievementRepository.saveAll(userAchievements);
        log.info("[AchievementEvaluationService] 닉네임 스냅샷 갱신: userId={}, count={}",
                userId, userAchievements.size());
    }

    private Optional<UserAchievement> evaluateSingle(Achievement achievement,
                                                      EvaluateAchievementsCommand command) {
        AchievementConditionStrategy strategy = strategyMap.get(achievement.getConditionType());
        if (strategy == null) {
            log.warn("[AchievementEvaluationService] 전략 없음: conditionType={}", achievement.getConditionType());
            return Optional.empty();
        }
        if (!strategy.isSatisfied(achievement, command)) return Optional.empty();

        UserAchievement userAchievement = UserAchievement.achieve(
                achievement,
                command.getUserId(),
                command.getUserNickname() != null ? command.getUserNickname() : "알 수 없음",
                command.getSeasonId(),
                command.getSeasonNumber() != null ? command.getSeasonNumber() : 0
        );
        userAchievementRepository.save(userAchievement);

        AchievementBadge badge = AchievementBadge.issue(userAchievement);
        achievementBadgeRepository.save(badge);

        log.info("[AchievementEvaluationService] 업적 달성: userId={}, achievement={}, season={}",
                command.getUserId(), achievement.getName(), command.getSeasonId());

        eventPublisher.publishAchievementUnlocked(
                command.getUserId(),
                command.getSeasonId(),
                command.getSeasonNumber(),
                command.getUserNickname(),
                command.getUserProfileImage(),
                achievement.getName(),
                achievement.getCategory().name(),
                achievement.getDifficulty().name()
        );

        return Optional.of(userAchievement);
    }
}
