package com.smarttasks.service;

import com.smarttasks.dto.AuthResponse;
import com.smarttasks.dto.LoginRequest;
import com.smarttasks.dto.RegisterRequest;
import com.smarttasks.entity.User;
import com.smarttasks.exception.BadRequestException;
import com.smarttasks.repository.UserRepository;
import com.smarttasks.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities("ROLE_USER")
                .build();

        String token = jwtTokenUtil.generateToken(userDetails);

        return new AuthResponse(token, savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        log.info("User attempting to login with email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        log.info("User logged in successfully: {}", user.getUsername());

        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail());
    }
}
