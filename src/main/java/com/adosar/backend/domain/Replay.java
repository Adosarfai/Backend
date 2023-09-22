package com.adosar.backend.domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Replay {
    private Integer replayId;
    private List<Integer> pauses;
    private List<Integer> timings;
}
