package com.blueride.coreservice.infrastructure.persistence.mapper;

import com.blueride.coreservice.domain.entity.User;
import com.blueride.coreservice.infrastructure.persistence.entity.UserJpaEntity;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(entity.getId(), entity.getUsername(), entity.getPassword());
    }

    public static UserJpaEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        return new UserJpaEntity(domain.getId(), domain.getUsername(), domain.getPassword());
    }
}
