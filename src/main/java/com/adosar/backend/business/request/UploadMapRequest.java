package com.adosar.backend.business.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadMapRequest {

    @Setter
    @NotNull
    private MultipartFile file;

    @Setter
    @NotNull
    private Integer mapId;

    @Setter
    @NotNull
    private String jwt;

}
