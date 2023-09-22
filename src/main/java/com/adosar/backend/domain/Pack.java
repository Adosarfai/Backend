package com.adosar.backend.domain;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pack {
    private User user;
    private Integer packId;
    @Setter private String title;
    @Setter private List<Map> maps;
    @Setter private Removed removed;
    @Setter private boolean published;
    @Setter private String removalReason;
}
