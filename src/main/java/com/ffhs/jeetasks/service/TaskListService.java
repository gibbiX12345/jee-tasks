package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.bean.LoginBean;
import com.ffhs.jeetasks.entity.TaskList;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class TaskListService {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<TaskList> findAllTaskLists() {
        return entityManager.createQuery("SELECT l FROM TaskList l", TaskList.class).getResultList();
    }

    public void addTaskList(TaskList taskList) {
        entityManager.persist(taskList);
    }
}
