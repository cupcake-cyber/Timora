package com.timora.app.service;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.ui.UserSessionDTO;

public interface SessionService {

    UserSessionDTO getCurrentSession(CurrentUser currentUser);
}