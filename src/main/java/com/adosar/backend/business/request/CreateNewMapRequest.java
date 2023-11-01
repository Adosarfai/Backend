package com.adosar.backend.business.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewMapRequest {

    @Setter
    @NotNull
    private String title;

    @Setter
    @NotNull
    private String artist;

    @Setter
    @NotNull
    private Boolean published;

}
