package com.adosar.backend.business.response;

import com.adosar.backend.domain.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMapByIdResponse {

    @Nullable
    private Map map;

    @NotNull
    @JsonIgnore
    private HttpStatus httpStatus;

}
