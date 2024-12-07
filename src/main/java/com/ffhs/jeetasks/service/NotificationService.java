package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Notification;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing notifications.
 */
@Stateless
public class NotificationService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    /**
     * Retrieves all notifications for the currently logged-in user that have not been dismissed.
     *
     * @return A list of undismissed notifications for the logged-in user, or an empty list if no user is logged in.
     */
    public List<Notification> findAllNotificationsForUserNotDismissed() {
        if (!SessionUtils.isLoggedIn()) return new ArrayList<>();
        Long userId = SessionUtils.getLoggedInUser().getUserId();
        return entityManager.createQuery("SELECT n FROM Notification n " +
                        "WHERE n.user.userId = :userId AND n.dismissed = false " +
                        "ORDER BY n.createdAt", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * Creates and persists a new notification for a specified recipient.
     *
     * @param text The notification text to display.
     * @param recipient The user who will receive the notification.
     * @param link An optional link associated with the notification.
     */
    public void createNotification(String text, User recipient, String link) {
        Notification notification = new Notification();
        notification.setText(text);
        notification.setUser(recipient);
        notification.setLink(link);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        insertModel(notification);
    }

    /**
     * Persists a new notification entity in the database.
     *
     * @param notification The notification to be saved.
     */
    public void insertModel(Notification notification) {
        entityManager.persist(notification);
    }

    /**
     * Updates an existing notification entity in the database.
     *
     * @param notification The notification to be updated.
     */
    public void updateModel(Notification notification) {
        entityManager.merge(notification);
    }
}
