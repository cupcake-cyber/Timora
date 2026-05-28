package com.timora.app.service;

import java.util.List;

public interface AuthorizationService {

    List<String> getPermissions(
            Long userId,
            Long supplierId
    );

    boolean hasPermission(
            Long userId,
            Long supplierId,
            String permission
    );
}