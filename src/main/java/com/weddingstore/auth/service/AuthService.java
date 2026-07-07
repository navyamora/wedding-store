package com.weddingstore.auth.service;

import com.weddingstore.auth.dto.AuthResponse;
import com.weddingstore.auth.dto.LoginRequest;
import com.weddingstore.auth.dto.RegisterRequest;
import com.weddingstore.common.security.JwtService;
import com.weddingstore.user.entity.Role;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
				this.userRepository = userRepository;
				this.passwordEncoder = passwordEncoder;
				this.jwtService = jwtService;
			}

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                "User registered successfully",
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                token
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                "Login successful",
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }
}
    