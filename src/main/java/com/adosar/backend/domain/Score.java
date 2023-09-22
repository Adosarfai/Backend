package com.adosar.backend.domain;

import lombok.*;

import java.util.Date;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Score {
    private Map map;
    private User user;
    private Float speed;
    private Date timeSet;
    private Replay replay;
    private Integer score;
    private Integer scoreId;
}
