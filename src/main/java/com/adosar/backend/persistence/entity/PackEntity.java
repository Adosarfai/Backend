package com.adosar.backend.persistence.entity;

import com.adosar.backend.domain.Removed;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
@Entity
@Table(name = "pack")
public class PackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false, updatable = false)
    private Integer packId;

    @ManyToOne()
    @JoinColumn(name = "user", nullable = false, updatable = false)
    private UserEntity user;

    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @Column(name = "published", nullable = false)
    private Boolean published = false;

    @Column(name = "removed", nullable = false)
    private Removed removed = Removed.NOT_REMOVED;

    @Column(name = "removal_reason", length = 500)
    private String removalReason;

    @ManyToMany
    @JoinColumn(name = "id")
    private List<MapEntity> maps;
}
