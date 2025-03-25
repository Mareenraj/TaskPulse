package com.mareen.userservice.service;

import com.mareen.userservice.dto.SignInRequest;
import com.mareen.userservice.dto.SignUpRequest;
import com.mareen.userservice.dto.TokenResponse;
import com.mareen.userservice.enums.RoleEnum;
import com.mareen.userservice.exception.InvalidTokenException;
import com.mareen.userservice.exception.RoleNotExistException;
import com.mareen.userservice.exception.UserAlreadyExistException;
import com.mareen.userservice.exception.UserNotExistException;
import com.mareen.userservice.model.Role;
import com.mareen.userservice.model.User;
import com.mareen.userservice.records.RoleAssignmentRequest;
import com.mareen.userservice.repository.RoleRepository;
import com.mareen.userservice.repository.UserRepository;
import com.mareen.userservice.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final SecurityExpressionHandler securityExpressionHandler;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, SecurityExpressionHandler securityExpressionHandler) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.securityExpressionHandler = securityExpressionHandler;
    }

    @Transactional
    public User signUp(SignUpRequest signUpRequest) {
        if (userRepository.findByUserName(signUpRequest.getUserName()).isPresent()) {
            throw new UserAlreadyExistException("This username already taken!");
        }
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("This email address already registered!");
        }
        User user = new User();
        user.setUserName(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        LocalDateTime currentDateTime = LocalDateTime.now();
        user.setCreatedDate(currentDateTime);
        user.setLastModifiedDate(currentDateTime);

        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Default role not configured"));
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    public TokenResponse SignIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUserName(), signInRequest.getPassword())
        );
        String accessToken = jwtTokenProvider.generateJwtToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateJwtToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token!");
        }
        String username = jwtTokenProvider.getUserNameFromJwtToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String newAccessToken = jwtTokenProvider.generateJwtToken(authentication);
        return new TokenResponse(newAccessToken, refreshToken);
    }

    public void assignRole(RoleAssignmentRequest roleAssignmentRequest) {
        User user = userRepository.findByUserName(roleAssignmentRequest.username())
                .orElseThrow(() -> new UserNotExistException("User not found"));

        Role role = roleRepository.findByRole(RoleEnum.valueOf(String.valueOf(roleAssignmentRequest.role())))
                .orElseThrow(() -> new RoleNotExistException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);
    }
}
