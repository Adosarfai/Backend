package com.adosar.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private Date CreationDate;
    @Setter private String username;
    @Setter private Privilege privilege;
    @JsonIgnore @Setter private String email;
    @JsonIgnore @Setter private String password;
}
