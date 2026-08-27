package com.blueride.coreservice.application.service;

import com.blueride.coreservice.application.usecase.CreateUserUseCase;
import com.blueride.coreservice.application.usecase.GetUserUseCase;
import com.blueride.coreservice.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;

    public UserApplicationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(String username, String password) {
        return CreateUserUseCase.execute(userRepository, username, password);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        return GetUserUseCase.execute(userRepository, id);
    }
}
