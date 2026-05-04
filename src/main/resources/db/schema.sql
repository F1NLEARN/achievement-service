-- ============================================================
-- achievementdb 스키마
-- Hibernate ddl-auto: update 가 자동 생성하므로 직접 실행 불필요
-- 테이블을 초기화하거나 수동으로 재생성할 때 사용
-- ============================================================

-- 연결 대상: achievementdb
-- \c achievementdb

-- ──────────────────────────────────────────────────────────────
-- 테이블 삭제 (초기화용)
-- ──────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS achievement_badges CASCADE;
DROP TABLE IF EXISTS user_achievements CASCADE;
DROP TABLE IF EXISTS achievements CASCADE;

-- ──────────────────────────────────────────────────────────────
-- achievements
--   서비스 시작 시 사전 정의된 업적 목록으로 초기화됨
--   카테고리(STOCK/ETF) × 난이도(BRONZE/SILVER/GOLD) 조합으로 구성
-- ──────────────────────────────────────────────────────────────
CREATE TABLE achievements (
    achievement_id   UUID            PRIMARY KEY,
    name             VARCHAR(100)    NOT NULL,
    category         VARCHAR(20)     NOT NULL CHECK (category IN ('STOCK', 'ETF')),
    difficulty       VARCHAR(20)     NOT NULL CHECK (difficulty IN ('BRONZE', 'SILVER', 'GOLD')),
    condition_type   VARCHAR(30)     NOT NULL CHECK (condition_type IN ('FIRST_BUY', 'HOLD_COUNT', 'RETURN_RATE')),
    condition_value  DECIMAL(10, 2)  NOT NULL,
    created_at       TIMESTAMP,
    created_by       UUID,
    updated_at       TIMESTAMP,
    updated_by       UUID,
    deleted_at       TIMESTAMP,
    deleted_by       UUID
);

-- ──────────────────────────────────────────────────────────────
-- user_achievements
--   유저별 업적 달성 기록
--   업적명/카테고리/난이도를 VO 스냅샷으로 보관하여 조회 시 JOIN 불필요
--   유니크 제약: 동일 시즌 내 동일 업적 중복 달성 방지
-- ──────────────────────────────────────────────────────────────
CREATE TABLE user_achievements (
    user_achievement_id     UUID        PRIMARY KEY,
    achievement_id          UUID        NOT NULL REFERENCES achievements(achievement_id),
    achievement_name        VARCHAR(100) NOT NULL,
    achievement_category    VARCHAR(20) NOT NULL,
    achievement_difficulty  VARCHAR(20) NOT NULL,
    user_id                 UUID        NOT NULL,
    user_nickname           VARCHAR(50) NOT NULL,
    season_id               UUID        NOT NULL,
    season_number           INT         NOT NULL,
    achieved_at             TIMESTAMP   NOT NULL,
    created_at              TIMESTAMP,
    created_by              UUID,
    updated_at              TIMESTAMP,
    updated_by              UUID,
    deleted_at              TIMESTAMP,
    deleted_by              UUID,

    CONSTRAINT uk_user_achievements_user_achievement_season
        UNIQUE (user_id, achievement_id, season_id)
);

-- ──────────────────────────────────────────────────────────────
-- achievement_badges
--   업적 달성 시 발급되는 뱃지
--   user_achievement 없이는 독립 존재 불가
-- ──────────────────────────────────────────────────────────────
CREATE TABLE achievement_badges (
    achievement_badge_id  UUID        PRIMARY KEY,
    user_achievement_id   UUID        NOT NULL REFERENCES user_achievements(user_achievement_id),
    user_id               UUID        NOT NULL,
    badge_name            VARCHAR(100) NOT NULL,
    badge_image_url       VARCHAR(500),
    paid_at               TIMESTAMP   NOT NULL,
    created_at            TIMESTAMP,
    created_by            UUID,
    updated_at            TIMESTAMP,
    updated_by            UUID,
    deleted_at            TIMESTAMP,
    deleted_by            UUID
);
