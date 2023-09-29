package com.adosar.backend.business.impl;

import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
final class UserConverter {

    public static User convert(UserEntity user) {
        return User.builder()
                .email(user.getEmail())
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .privilege(user.getPrivilege())
                .CreationDate(user.getCreationDate())
                .build();
    }
}
