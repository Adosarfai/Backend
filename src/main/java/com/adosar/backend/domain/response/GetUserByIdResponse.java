package com.adosar.backend.domain.response;

import com.adosar.backend.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserByIdResponse {
    @Nullable private User user;
    private HttpStatus httpStatus;
    
    
}
