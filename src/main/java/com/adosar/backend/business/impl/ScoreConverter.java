package com.adosar.backend.business.impl;

import com.adosar.backend.domain.Score;
import com.adosar.backend.persistence.entity.ScoreEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
final class ScoreConverter {

    public static ScoreEntity convert(Score score) {
        assert score != null;
        return ScoreEntity.builder()
                .scoreId(score.getScoreId())
                .map(MapConverter.convert(score.getMap()))
                .user(UserConverter.convert(score.getUser()))
                .replay(ReplayConverter.convert(score.getReplay()))
                .timeSet(score.getTimeSet())
                .speed(score.getSpeed())
                .points(score.getScore())
                .build();
    }

    public static Score convert(ScoreEntity score) {
        assert score != null;
        return Score.builder()
                .scoreId(score.getScoreId())
                .map(MapConverter.convert(score.getMap()))
                .user(UserConverter.convert(score.getUser()))
                .replay(ReplayConverter.convert(score.getReplay()))
                .timeSet(score.getTimeSet())
                .speed(score.getSpeed())
                .score(score.getPoints())
                .build();
    }
}
