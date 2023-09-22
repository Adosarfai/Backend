package com.adosar.backend.domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Leaderboard {
    private User user;
    private Integer leaderboardId;
    @Setter private List<Map> maps;
    @Setter private List<User> players;
}
