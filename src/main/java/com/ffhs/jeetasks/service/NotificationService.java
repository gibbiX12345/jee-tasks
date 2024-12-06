package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.bean.LoginBean;
import com.ffhs.jeetasks.entity.Notification;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.entity.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
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
    @Inject
    private LoginBean loginBean;

    public List<Notification> findAllNotificationsForUserNotDismissed() {
        if (loginBean.getUser() == null) return new ArrayList<>();
        Long userId = loginBean.getUser().getUserId();
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
