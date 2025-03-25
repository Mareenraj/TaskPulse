package com.mareen.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "username is mandatory!")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters")
    private String userName;

    @NotBlank(message = "password is mandatory!")
    @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
    private String password;

    @NotBlank(message = "Email is mandatory!")
    @Email(message = "Email should be valid!")
    private String email;
}
