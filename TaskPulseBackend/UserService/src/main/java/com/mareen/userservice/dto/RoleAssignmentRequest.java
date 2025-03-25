package com.mareen.userservice.dto;

import com.mareen.userservice.enums.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoleAssignmentRequest(
        @NotBlank String username,
        @NotNull RoleEnum role
) {}
