package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.bean.LoginBean;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.TaskList;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class TaskListService {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;
    @Inject
    private LoginBean loginBean;

    public List<TaskList> findAllTaskLists() {
        return entityManager.createQuery("SELECT l FROM TaskList l", TaskList.class).getResultList();
    }

    public List<TaskList> findAllTaskListsForUser() {
        if (loginBean.getUser() == null) return new ArrayList<>();
        Long userId = loginBean.getUser().getUserId();
        return entityManager.createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId", TaskList.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void addTaskList(TaskList taskList) {
        entityManager.persist(taskList);
    }
}
