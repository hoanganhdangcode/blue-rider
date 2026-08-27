package com.blueride.coreservice.presentation.rest;

import com.blueride.coreservice.application.service.UserApplicationService;
import com.blueride.coreservice.application.service.UserResponse;
import com.blueride.coreservice.common.BaseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    public BaseResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = userApplicationService.createUser(request.username(), request.password());
        return BaseResponse.success("User created successfully", user);
    }

    @GetMapping("/{id}")
    public BaseResponse<UserResponse> getUser(@PathVariable Long id) {
        UserResponse user = userApplicationService.getUser(id);
        return BaseResponse.success(user);
    }

    public record CreateUserRequest(
            @NotBlank(message = "must not be blank")
            String username,
            @NotBlank(message = "must not be blank")
            @Size(min = 6, message = "must be at least 6 characters")
            String password) {
    }
}
