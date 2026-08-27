package com.blueride.coreservice.infrastructure.persistence.repository;

import com.blueride.coreservice.domain.entity.Account;
import com.blueride.coreservice.domain.repository.AccountRepository;
import com.blueride.coreservice.infrastructure.persistence.entity.AccountJpaEntity;
import com.blueride.coreservice.infrastructure.persistence.mapper.AccountMapper;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, Long> {

    Optional<AccountJpaEntity> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    long countByPhoneNumber(String phoneNumber);

    /**
     * Atomic insert that performs no-op when the phone number already exists.
     * Requires PostgreSQL (or an H2 instance in PostgreSQL mode) and backs
     * {@link AccountRepository#findOrCreateByPhoneNumber}.
     */
    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO account (phone_number, status, created_at, updated_at) "
                    + "VALUES (:phone, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) "
                    + "ON CONFLICT (phone_number) DO NOTHING")
    int insertIfAbsent(@Param("phone") String phoneNumber);
}

@Repository
class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository jpaRepository;

    AccountRepositoryImpl(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return jpaRepository.findById(id).map(AccountMapper::toDomain);
    }

    @Override
    public Optional<Account> findByPhoneNumber(String phoneNumber) {
        return jpaRepository.findByPhoneNumber(phoneNumber).map(AccountMapper::toDomain);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return jpaRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public long countByPhoneNumber(String phoneNumber) {
        return jpaRepository.countByPhoneNumber(phoneNumber);
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = jpaRepository.save(AccountMapper.toEntity(account));
        return AccountMapper.toDomain(entity);
    }

    @Override
    public Account findOrCreateByPhoneNumber(String phoneNumber) {
        jpaRepository.insertIfAbsent(phoneNumber);
        AccountJpaEntity entity = jpaRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalStateException(
                        "Account insert/re-read failed for phone " + phoneNumber));
        return AccountMapper.toDomain(entity);
    }
}
