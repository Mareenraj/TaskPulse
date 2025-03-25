package com.mareen.userservice.repository;

import com.mareen.userservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(@NotBlank(message = "Email is mandatory.") @Email(message = "Email should be valid.") String email);
}
