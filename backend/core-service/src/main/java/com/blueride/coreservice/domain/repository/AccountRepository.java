package com.blueride.coreservice.domain.repository;

import com.blueride.coreservice.domain.entity.Account;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(Long id);

    Optional<Account> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    long countByPhoneNumber(String phoneNumber);

    Account save(Account account);

    /**
     * Returns the account with the given phone number, creating it if it does not
     * exist. Concurrent invocations never create duplicates because creation relies
     * on the database unique constraint on {@code phone_number}; the loser of a race
     * re-reads the winner's row instead of failing.
     */
    Account findOrCreateByPhoneNumber(String phoneNumber);
}
