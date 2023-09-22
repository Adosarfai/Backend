package com.adosar.backend.domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Map {
    private User user;
    private Integer mapId;
    private Date creationDate;
    @Setter private String hash;
    @Setter private String title;
    @Setter private String artist;
    @Setter private Date lastUpdate;
    @Setter private Removed removed;
    @Setter private Boolean published;
    @Setter private String removalReason;
}
