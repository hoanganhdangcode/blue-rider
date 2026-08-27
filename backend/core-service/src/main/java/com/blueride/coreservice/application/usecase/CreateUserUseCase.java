package com.blueride.coreservice.application.usecase;

import com.blueride.coreservice.application.service.UserResponse;
import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;
import com.blueride.coreservice.domain.entity.User;
import com.blueride.coreservice.domain.repository.UserRepository;

public final class CreateUserUseCase {

    private CreateUserUseCase() {
    }

    public static UserResponse execute(UserRepository repository, String username, String password) {
        User user = User.create(username, password);

        if (repository.existsByUsername(user.getUsername())) {
            throw new AppException(ResponseStatus.CONFLICT, "Username already exists: " + user.getUsername());
        }

        User saved = repository.save(user);
        return UserResponse.from(saved);
    }
}
