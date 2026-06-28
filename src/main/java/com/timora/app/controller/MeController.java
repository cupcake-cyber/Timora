package com.timora.app.controller;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.ui.UserSessionDTO;
import com.timora.app.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MeController {

    private final SessionService sessionService;

    @GetMapping("/me")
    public UserSessionDTO me(@AuthenticationPrincipal CurrentUser currentUser) {

        return sessionService.getCurrentSession(currentUser);
    }
}