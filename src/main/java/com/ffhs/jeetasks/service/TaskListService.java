package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.dto.TaskFormDTO;
import com.ffhs.jeetasks.dto.TaskListFormDTO;
import com.ffhs.jeetasks.entity.Task;
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

    public TaskListFormDTO toFormDTO(TaskList taskList) {
        TaskListFormDTO dto = new TaskListFormDTO();
        dto.setTitle(taskList.getTitle());
        dto.setDescription(taskList.getDescription());
        return dto;
    }

    public void updateTaskListFromDTO(TaskList taskList, TaskListFormDTO dto) {
        taskList.setTitle(dto.getTitle());
        taskList.setDescription(dto.getDescription());
    }

    public void insertModel(TaskList taskList) {
        entityManager.persist(taskList);
    }

    public void updateModel(TaskList taskList) {
        entityManager.merge(taskList);
    }
}
