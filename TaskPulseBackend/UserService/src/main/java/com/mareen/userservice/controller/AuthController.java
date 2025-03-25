package com.mareen.userservice.controller;

import com.mareen.userservice.dto.RoleAssignmentRequest;
import com.mareen.userservice.dto.SignInRequest;
import com.mareen.userservice.dto.SignUpRequest;
import com.mareen.userservice.dto.TokenResponse;
import com.mareen.userservice.exception.InvalidTokenException;
import com.mareen.userservice.exception.RoleNotExistException;
import com.mareen.userservice.exception.UserAlreadyExistException;
import com.mareen.userservice.exception.UserNotExistException;
import com.mareen.userservice.model.User;
import com.mareen.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = authService.signUp(signUpRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<TokenResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        TokenResponse tokenResponse = authService.SignIn(signInRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestParam String refreshToken) {
        TokenResponse tokenResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRole(@Valid @RequestBody RoleAssignmentRequest roleAssignmentRequest) {
        authService.assignRole(roleAssignmentRequest);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidToken(InvalidTokenException invalidTokenException) {
        return new ResponseEntity<>(invalidTokenException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistException userAlreadyExistException) {
        return new ResponseEntity<>(userAlreadyExistException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<String> handleInvalidToken(UserNotExistException userNotExistException) {
        return new ResponseEntity<>(userNotExistException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoleNotExistException.class)
    public ResponseEntity<String> handleUserAlreadyExists(RoleNotExistException roleNotExistException) {
        return new ResponseEntity<>(roleNotExistException.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
