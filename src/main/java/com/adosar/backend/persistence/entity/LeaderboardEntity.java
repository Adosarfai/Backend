package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Entity
@Table(name = "leaderboard")
public class LeaderboardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false, updatable = false)
    private Integer leaderboardId;

    @ManyToOne()
    @JoinColumn(name = "user", nullable = false, updatable = false)
    private UserEntity user;

    @ManyToMany
    private List<MapEntity> maps;

    @ManyToMany
    private List<UserEntity> players;
}
