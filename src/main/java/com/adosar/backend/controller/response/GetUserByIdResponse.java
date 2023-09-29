package com.adosar.backend.controller.response;

import com.adosar.backend.domain.User;
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
public class GetUserByIdResponse {
    
    @Nullable
    private User user;

    @NotNull
    private HttpStatus httpStatus;


}
