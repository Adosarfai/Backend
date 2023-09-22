package com.adosar.backend.persistence.entity;

import com.adosar.backend.domain.Removed;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@ToString
@Entity
@Table(name = "map")
public class MapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false, updatable = false)
    private Integer mapId;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false, updatable = false)
    private UserEntity user;

    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @Column(name = "artist", length = 100, nullable = false)
    private String artist;

    @Column(name = "published", nullable = false)
    private Boolean published = false;

    @Column(name = "removed", nullable = false)
    private Removed removed = Removed.NOT_REMOVED;

    @Column(name = "removal_reason", length = 500)
    private String removalReason;

    @Column(name = "hash", length = 64, nullable = false, updatable = false)
    private String hash;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    @JsonCreator
    public MapEntity() {
    }
}
