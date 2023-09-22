package com.adosar.backend.business.impl;

import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
final class UserConverter {
    
    public static User convert(UserEntity user) {
        return User.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .CreationDate(user.getCreationDate())
                .privilege(user.getPrivilege())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }
}
