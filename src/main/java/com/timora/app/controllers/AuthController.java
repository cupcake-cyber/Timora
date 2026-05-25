package com.timora.app.controllers;

import com.timora.app.models.User;
import com.timora.app.repository.UserRepository;
import com.timora.app.security.AuthRequest;
import com.timora.app.security.AuthResponse;
import com.timora.app.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request
    ) {

        User user = userRepository
                .findByLoginEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!matches) {
            throw new RuntimeException("Password incorrecto");
        }

        String token = jwtService.generateToken(
                user.getLoginEmail(),
                user.getCompany().getId(),
                user.getGlobalRole().name()
        );

        return new AuthResponse(token);
    }
}
