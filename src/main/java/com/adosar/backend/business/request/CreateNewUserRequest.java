package com.adosar.backend.business.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewUserRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 3, max = 50)
    private String username;

    @NotNull
    @Length(min = 10)
    private String password;
}
