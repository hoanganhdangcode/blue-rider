package com.blueride.coreservice.infrastructure.persistence.repository;

import com.blueride.coreservice.domain.repository.UserRepository;
import com.blueride.coreservice.domain.entity.User;
import com.blueride.coreservice.infrastructure.persistence.entity.UserJpaEntity;
import com.blueride.coreservice.infrastructure.persistence.mapper.UserMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}

@Repository
class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = jpaRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(entity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }
}
