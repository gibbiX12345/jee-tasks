package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TaskListService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<TaskList> findAllTaskLists() {
        return entityManager.createQuery("SELECT l FROM TaskList l", TaskList.class).getResultList();
    }

    public List<TaskList> findAllTaskListsForUser() {
        if (!SessionUtils.isLoggedIn()) return new ArrayList<>();
        Long userId = SessionUtils.getLoggedInUser().getUserId();
        return entityManager.createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId ORDER BY l.createdAt", TaskList.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void insertModel(TaskList taskList) {
        entityManager.persist(taskList);
    }

    public void updateModel(TaskList taskList) {
        entityManager.merge(taskList);
    }
}
