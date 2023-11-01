package com.adosar.backend.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pack {

    @NotNull
    private User user;

    @NotNull
    @Id
    private Integer packId;

    @Setter
    @NotNull
    private String title;

    @Setter
    @NotNull
    private List<Map> maps;

    @Setter
    @NotNull
    private Removed removed;

    @Setter
    @NotNull
    private boolean published;

    @Setter
    @Nullable
    private String removalReason;
}
