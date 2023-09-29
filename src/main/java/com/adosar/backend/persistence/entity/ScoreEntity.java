package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@Entity
@Table(name = "score")
public class ScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false, updatable = false)
    private Integer scoreId;

    @ManyToOne
    @JoinColumn(name = "map", nullable = false, updatable = false)
    private MapEntity map;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false, updatable = false)
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "replay", nullable = false, updatable = false)
    private ReplayEntity replay;

    @Column(name = "time_set", nullable = false, updatable = false)
    private Date timeSet;

    @Column(name = "speed", nullable = false, updatable = false)
    private Float speed;

    @Column(name = "score", nullable = false, updatable = false)
    private Integer score;
}
