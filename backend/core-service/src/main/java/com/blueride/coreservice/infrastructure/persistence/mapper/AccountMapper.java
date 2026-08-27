package com.blueride.coreservice.infrastructure.persistence.mapper;

import com.blueride.coreservice.domain.entity.Account;
import com.blueride.coreservice.infrastructure.persistence.entity.AccountJpaEntity;

public final class AccountMapper {

    private AccountMapper() {
    }

    public static Account toDomain(AccountJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Account(entity.getId(), entity.getPhoneNumber(), entity.getStatus(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public static AccountJpaEntity toEntity(Account domain) {
        if (domain == null) {
            return null;
        }
        return new AccountJpaEntity(domain.getId(), domain.getPhoneNumber(), domain.getStatus(),
                domain.getCreatedAt(), domain.getUpdatedAt());
    }
}
