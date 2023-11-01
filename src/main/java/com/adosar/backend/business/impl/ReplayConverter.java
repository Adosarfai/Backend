package com.adosar.backend.business.impl;

import com.adosar.backend.domain.Replay;
import com.adosar.backend.persistence.entity.ReplayEntity;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor
final class ReplayConverter {

    public static ReplayEntity convert(Replay replay) {
        assert replay != null;
        return ReplayEntity.builder()
                .replayId(replay.getReplayId())
                .pauses(replay.getPauses().stream().mapToInt(i -> i).toArray())
                .timings(replay.getTimings().stream().mapToInt(i -> i).toArray())
                .build();
    }

    public static Replay convert(ReplayEntity replay) {
        assert replay != null;
        return Replay.builder()
                .replayId(replay.getReplayId())
                .pauses(Arrays.stream(replay.getPauses()).boxed().toList())
                .timings(Arrays.stream(replay.getTimings()).boxed().toList())
                .build();
    }
}
