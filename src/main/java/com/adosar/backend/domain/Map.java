package com.adosar.backend.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Map {

    @NotNull
    private User user;

    @NotNull
    @Id
    private Integer mapId;

    @NotNull
    private Date creationDate;

    @Setter
    @Nullable
    private String hash;

    @Setter
    @NotNull
    private String title;

    @Setter
    @NotNull
    private String artist;

    @Setter
    @NotNull
    private Date lastUpdate;

    @Setter
    @NotNull
    private Removed removed;

    @Setter
    @NotNull
    private Boolean published;

    @Setter
    @Nullable
    private String removalReason;
}
