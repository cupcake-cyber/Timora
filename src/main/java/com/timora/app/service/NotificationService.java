package com.timora.app.service;

import com.timora.app.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    List<Notification> findAll();

    Optional<Notification> findById(Long id);

    Notification save(Notification notification);

    Notification update(Long id, Notification notification);

    void delete(Long id);
}