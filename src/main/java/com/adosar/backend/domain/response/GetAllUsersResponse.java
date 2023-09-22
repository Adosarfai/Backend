package com.adosar.backend.domain.response;

import com.adosar.backend.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersResponse {
    @Nullable private List<User> users;
    private HttpStatus httpStatus;
    
    
}
