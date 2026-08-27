package com.blueride.coreservice.domain.repository;

import com.blueride.coreservice.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    User save(User user);

    boolean existsByUsername(String username);
}
