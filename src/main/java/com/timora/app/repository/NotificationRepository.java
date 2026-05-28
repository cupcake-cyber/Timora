package com.timora.app.repository;

import com.timora.app.model.Notification;
import com.timora.app.model.enums.NotificationStatus;
import com.timora.app.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // =========================
    // BASE USER NOTIFICATIONS
    // =========================

    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.user.id = :userId
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findByUser(@Param("userId") Long userId);

    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.user.id = :userId
        AND n.isRead = false
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findUnreadByUser(@Param("userId") Long userId);

    // =========================
    // TYPE FILTER
    // =========================

    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.user.id = :userId
        AND n.type = :type
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findByUserAndType(
            @Param("userId") Long userId,
            @Param("type") NotificationType type
    );

    // =========================
    // STATUS FILTER
    // =========================

    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.user.id = :userId
        AND n.status = :status
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findByUserAndStatus(
            @Param("userId") Long userId,
            @Param("status") NotificationStatus status
    );

    // =========================
    // TYPE + STATUS (ADVANCED FILTER)
    // =========================

    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.type = :type
        AND n.status = :status
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findByTypeAndStatus(
            @Param("type") NotificationType type,
            @Param("status") NotificationStatus status
    );

    // =========================
    // SYSTEM QUERY (ALL BY TYPE)
    // =========================

    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.type = :type
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findAllByType(
            @Param("type") NotificationType type
    );
}