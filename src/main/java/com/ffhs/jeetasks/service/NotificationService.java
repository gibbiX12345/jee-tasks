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

@Stateless
public class NotificationService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<Notification> findAllNotificationsForUserNotDismissed() {
        if (!SessionUtils.isLoggedIn()) return new ArrayList<>();
        Long userId = SessionUtils.getLoggedInUser().getUserId();
        return entityManager.createQuery("SELECT n FROM Notification n " +
                        "WHERE n.user.userId = :userId AND n.dismissed = false " +
                        "ORDER BY n.createdAt", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void createNotification(String text, User recipient, String link) {
        Notification notification = new Notification();
        notification.setText(text);
        notification.setUser(recipient);
        notification.setLink(link);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        insertModel(notification);
    }

    public void insertModel(Notification notification) {
        entityManager.persist(notification);
    }

    public void updateModel(Notification notification) {
        entityManager.merge(notification);
    }
}
