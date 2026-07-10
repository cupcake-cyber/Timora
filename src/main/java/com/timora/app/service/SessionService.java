package com.timora.app.service;

import com.timora.app.dto.ui.UserSessionDTO;

public interface SessionService {
    UserSessionDTO getCurrentSession();
}