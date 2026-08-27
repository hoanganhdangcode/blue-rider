package com.blueride.coreservice.application.usecase;

import com.blueride.coreservice.application.service.UserResponse;
import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;
import com.blueride.coreservice.domain.entity.User;
import com.blueride.coreservice.domain.repository.UserRepository;

public final class GetUserUseCase {

    private GetUserUseCase() {
    }

    public static UserResponse execute(UserRepository repository, Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new AppException(ResponseStatus.NOT_FOUND, "User not found: " + id));

        return UserResponse.from(user);
    }
}
