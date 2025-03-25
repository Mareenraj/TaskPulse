package com.mareen.userservice.repository;

import com.mareen.userservice.enums.RoleEnum;
import com.mareen.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum roleEnum);
}
