package com.adosar.backend.controller.response;

import com.adosar.backend.domain.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersResponse {
    
    @Nullable
    private List<User> users;
    
    @NotNull
    private HttpStatus httpStatus;


}