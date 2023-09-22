package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
@Table(name = "replay")
public class ReplayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false, updatable = false)
    private Integer replayId;

    @Column(name = "timings", nullable = false, updatable = false)
    private int[] timings;

    @Column(name = "pauses", nullable = false, updatable = false)
    private int[] pauses;
}
