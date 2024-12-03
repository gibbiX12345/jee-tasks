package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.bean.LoginBean;
import com.ffhs.jeetasks.entity.Task;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class TaskService {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;
    @Inject
    private LoginBean loginBean;

    public List<Task> findAllTasks() {
        return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    public List<Task> findAllTasksForUser() {
        Long userId = loginBean.getUserId();
        return entityManager.createQuery("SELECT t FROM Task t WHERE t.taskList.user.userId = :userId", Task.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
