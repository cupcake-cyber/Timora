package com.timora.app.controller;

import com.timora.app.dto.security.AuthResponseDTO;
import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.security.LoginRequest;
import com.timora.app.model.User;
import com.timora.app.security.jwt.JwtUtil;
import com.timora.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),

                        request.getPassword()
                )
        );

        User user = userService.findByEmail(request.getEmail());

        String token = jwtUtil.generateToken(user.getEmail());

        CurrentUser currentUser = userService.buildCurrentUser(user);

        return new AuthResponseDTO(
                token,
                "Bearer",
                currentUser
        );
    }
}