package com.adosar.backend.persistence.entity;

import com.adosar.backend.domain.Removed;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pack")
public class PackEntity {
    @Positive
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false, updatable = false)
    private Integer packId;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user", nullable = false, updatable = false)
    private UserEntity user;

    @Length(max = 250)
    @Size(max = 250)
    @NotBlank
    @NotEmpty
    @NotNull
    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @NotNull
    @Column(name = "published", nullable = false)
    private Boolean published = false;

    @NotNull
    @Enumerated
    @Column(name = "removed", nullable = false)
    private Removed removed = Removed.NOT_REMOVED;

    @Length(max = 2000)
    @Size(max = 2000)
    @Column(name = "removal_reason", length = 2000)
    private String removalReason;

    @Size(min = 1)
    @NotNull
    @ManyToMany
    @JoinColumn(name = "id")
    private List<MapEntity> maps;
}
