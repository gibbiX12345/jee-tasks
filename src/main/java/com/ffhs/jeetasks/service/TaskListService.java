package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.dto.TaskListFormDTO;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing task lists.
 * Provides methods for retrieving, updating, and persisting task list data.
 */
@Stateless
public class TaskListService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    /**
     * Retrieves all task lists for the currently logged-in user.
     *
     * @return A list of {@link TaskList} entities associated with the logged-in user.
     */
    public List<TaskList> findAllTaskListsForUser() {
        if (!SessionUtils.isLoggedIn()) {
            return new ArrayList<>();
        }
        Long userId = SessionUtils.getLoggedInUser().getUserId();
        return entityManager.createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId ORDER BY l.createdAt", TaskList.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * Converts a {@link TaskList} entity to a {@link TaskListFormDTO}.
     *
     * @param taskList The task list entity to convert.
     * @return A DTO representation of the task list.
     */
    public TaskListFormDTO toFormDTO(TaskList taskList) {
        TaskListFormDTO dto = new TaskListFormDTO();
        dto.setTitle(taskList.getTitle());
        dto.setDescription(taskList.getDescription());
        return dto;
    }

    /**
     * Updates a {@link TaskList} entity with data from a {@link TaskListFormDTO}.
     *
     * @param taskList The task list entity to update.
     * @param dto      The DTO containing the updated data.
     */
    public void updateTaskListFromDTO(TaskList taskList, TaskListFormDTO dto) {
        taskList.setTitle(dto.getTitle());
        taskList.setDescription(dto.getDescription());
    }

    /**
     * Persists a new {@link TaskList} entity to the database.
     *
     * @param taskList The task list to insert.
     */
    public void insertModel(TaskList taskList) {
        entityManager.persist(taskList);
    }

    /**
     * Updates an existing {@link TaskList} entity in the database.
     *
     * @param taskList The task list to update.
     */
    public void updateModel(TaskList taskList) {
        entityManager.merge(taskList);
    }
}
