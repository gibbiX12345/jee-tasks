package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.bean.LoginBean;
import com.ffhs.jeetasks.entity.Notification;
import com.ffhs.jeetasks.entity.TaskList;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
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

    public void insertModel(Notification notification) {
        entityManager.persist(notification);
    }

    public void updateModel(Notification notification) {
        entityManager.merge(notification);
    }
}
