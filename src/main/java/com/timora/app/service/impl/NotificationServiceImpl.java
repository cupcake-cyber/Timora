package com.timora.app.service.impl;

import com.timora.app.models.Notification;
import com.timora.app.repository.NotificationRepository;
import com.timora.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Long id, Notification notification) {

        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        existing.setUser(notification.getUser());
        existing.setType(notification.getType());
        existing.setMessage(notification.getMessage());
        existing.setStatus(notification.getStatus());
        existing.setSentAt(notification.getSentAt());
        existing.setTarget(notification.getTarget());

        return notificationRepository.save(existing);
    }

    @Override
    public void delete(Long id) {

        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notificationRepository.delete(existing);
    }
}