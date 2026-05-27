package com.timora.app.repository;

import com.timora.app.model.Notification;
import com.timora.app.model.enums.NotificationStatus;
import com.timora.app.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByUserIdAndStatus(
            Long userId,
            NotificationStatus status
    );

    List<Notification> findByUserIdAndType(
            Long userId,
            NotificationType type
    );

    List<Notification> findByUserIdOrderBySentAtDesc(Long userId);

    List<Notification> findByUserIdAndStatusNot(
            Long userId,
            NotificationStatus status
    );

    List<Notification> findByTypeOrderBySentAtDesc(
            NotificationType type
    );

    @Query("""
            SELECT n
            FROM Notification n
            JOIN n.user u
            WHERE u.id = :userId
            ORDER BY n.sentAt DESC
            """)
    List<Notification> getNotificationsByUser(
            @Param("userId") Long userId
    );

    @Query("""
            SELECT n
            FROM Notification n
            WHERE n.user.id = :userId
            AND n.status <> 'READ'
            ORDER BY n.sentAt DESC
            """)
    List<Notification> getUnreadNotifications(
            @Param("userId") Long userId
    );

    @Query("""
            SELECT n
            FROM Notification n
            WHERE n.type = :type
            AND n.status = :status
            ORDER BY n.sentAt DESC
            """)
    List<Notification> getNotificationsByTypeAndStatus(
            @Param("type") NotificationType type,
            @Param("status") NotificationStatus status
    );
}