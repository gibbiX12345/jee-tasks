package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.bean.LoginBean;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.TaskList;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TaskService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;
    @Inject
    private LoginBean loginBean;

    public List<Task> findAllTasks() {
        return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    public List<Task> findAllTasksForUser() {
        if (loginBean.getUser() == null) return new ArrayList<>();
        Long userId = loginBean.getUser().getUserId();
        return entityManager.createQuery("SELECT t FROM Task t WHERE t.taskList.user.userId = :userId", Task.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Task> findAllTasksByListId(Long listId) {
        if (listId == null) {
            return findAllTasksForUser();
        }
        return entityManager.createQuery("SELECT t FROM Task t WHERE t.taskList.listId = :listId", Task.class)
                .setParameter("listId", listId)
                .getResultList();
    }

    public void insertModel(Task taskList) {
        entityManager.persist(taskList);
    }

    public void updateModel(Task taskList) {
        entityManager.merge(taskList);
    }
}
