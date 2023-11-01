package com.adosar.backend.business.impl;

import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
final class UserConverter {

    public static UserEntity convert(User user) {
        assert user != null;
        return UserEntity.builder()
                .email(user.getEmail())
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .privilege(user.getPrivilege())
                .creationDate(user.getCreationDate())
                .build();
    }

    public static User convert(UserEntity user) {
        assert user != null;
        return User.builder()
                .email(user.getEmail())
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .privilege(user.getPrivilege())
                .creationDate(user.getCreationDate())
                .build();
    }
}
