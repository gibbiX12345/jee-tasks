package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.bean.TaskBean;
import com.ffhs.jeetasks.dto.TaskFormDTO;
import com.ffhs.jeetasks.entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class to handle task-related business logic and database operations.
 */
@Stateless
public class TaskService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Find a task by its ID.
     *
     * @param taskId The ID of the task.
     * @return The task entity.
     */
    public Task findTaskById(Long taskId) {
        return entityManager.find(Task.class, taskId);
    }

    /**
     * Find all tasks filtered by list ID, sorted, and filtered by user context.
     *
     * @param listId     The ID of the task list.
     * @param sortColumn The column to sort by.
     * @param ascending  Whether sorting is ascending.
     * @param type       The technical list type.
     * @param userId     The ID of the user.
     * @return A list of tasks.
     */
    public List<Task> findAllTasksByListId(Long listId, String sortColumn, boolean ascending, TaskBean.TECHNICAL_LIST_TYPE type, Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);

        Predicate filters = buildFilters(cb, task, listId, userId, type);
        Order order = buildOrder(cb, task, sortColumn, ascending);

        query.where(filters).orderBy(order);
        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Insert a new task into the database.
     *
     * @param task The task entity to insert.
     */
    public void insertModel(Task task) {
        entityManager.persist(task);
    }

    /**
     * Update an existing task in the database.
     *
     * @param task The task entity to update.
     */
    public void updateModel(Task task) {
        entityManager.merge(task);
    }

    /**
     * Delete a task from the database.
     *
     * @param task The task entity to delete.
     */
    public void deleteModel(Task task) {
        entityManager.remove(entityManager.merge(task));
    }

    /**
     * Group tasks by their status.
     *
     * @param tasks The list of tasks to group.
     * @return A map grouping tasks by their status.
     */
    public Map<Status, List<Task>> groupTasksByStatus(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        Task::getStatus,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    /**
     * Group tasks with a default status for tasks without a status.
     *
     * @param tasks         The list of tasks to group.
     * @param defaultStatus The default status to assign to tasks without a status.
     * @return A map grouping tasks by their status or the default status.
     */
    public Map<Status, List<Task>> groupTasksWithDefaultStatus(List<Task> tasks, Status defaultStatus) {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        task -> task.getStatus() != null ? task.getStatus() : defaultStatus,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    /**
     * Get a default status instance for grouping.
     *
     * @return A default status entity.
     */
    public Status getDefaultStatus() {
        Status defaultStatus = new Status();
        defaultStatus.setValue("No Status");
        defaultStatus.setOrder(Integer.MAX_VALUE);
        return defaultStatus;
    }

    /**
     * Convert a Task entity to a TaskFormDTO.
     *
     * @param task The task entity to convert.
     * @return A TaskFormDTO representing the task.
     */
    public TaskFormDTO toFormDTO(Task task) {
        TaskFormDTO dto = new TaskFormDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDateString(task.getDueDate() != null ? task.getDueDate().toLocalDateTime().format(FORMATTER) : null);
        dto.setAssignedUserId(task.getAssignedUser() != null ? task.getAssignedUser().getUserId() : null);
        dto.setPriorityId(task.getPriority() != null ? task.getPriority().getPriorityId() : null);
        dto.setStatusId(task.getStatus() != null ? task.getStatus().getStatusId() : null);
        return dto;
    }

    /**
     * Update an existing Task entity with data from a TaskFormDTO.
     *
     * @param task The task entity to update.
     * @param dto  The TaskFormDTO containing updated data.
     */
    public void updateTaskFromDTO(Task task, TaskFormDTO dto) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        // Parse and set due date
        if (dto.getDueDateString() != null && !dto.getDueDateString().isEmpty()) {
            LocalDateTime dueDate = LocalDateTime.parse(dto.getDueDateString(), FORMATTER);
            task.setDueDate(Timestamp.valueOf(dueDate));
        } else {
            task.setDueDate(null);
        }

        // Set assigned user
        if (dto.getAssignedUserId() != null) {
            User assignedUser = entityManager.find(User.class, dto.getAssignedUserId());
            task.setAssignedUser(assignedUser);
        } else {
            task.setAssignedUser(null);
        }

        // Set priority
        if (dto.getPriorityId() != null) {
            Priority priority = entityManager.find(Priority.class, dto.getPriorityId());
            task.setPriority(priority);
        } else {
            task.setPriority(null);
        }

        // Set status
        if (dto.getStatusId() != null) {
            Status status = entityManager.find(Status.class, dto.getStatusId());
            task.setStatus(status);
        } else {
            task.setStatus(null);
        }

        // Set created timestamp if creating a new task
        if (task.getCreatedAt() == null) {
            task.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
    }

    /**
     * Build filters for query based on list ID, user ID, and list type.
     *
     * @param cb     The CriteriaBuilder for constructing predicates.
     * @param task   The root task entity.
     * @param listId The ID of the task list.
     * @param userId The ID of the user.
     * @param type   The technical list type.
     * @return A Predicate representing the filters.
     */
    private Predicate buildFilters(CriteriaBuilder cb, Root<Task> task, Long listId, Long userId, TaskBean.TECHNICAL_LIST_TYPE type) {
        Predicate listIdPredicate = listId != null ? cb.equal(task.get("taskList").get("listId"), listId) : cb.conjunction();
        Predicate userIdPredicate = cb.equal(task.get("taskList").get("user").get("userId"), userId);
        Predicate assignedUserIdPredicate = cb.equal(task.get("assignedUser").get("userId"), userId);

        switch (type) {
            case CUSTOM_LIST:
                return listIdPredicate;
            case ALL_TASKS:
                return cb.or(userIdPredicate, assignedUserIdPredicate);
            case MY_ASSIGNED_TASKS:
                return assignedUserIdPredicate;
            default:
                return cb.conjunction();
        }
    }

    /**
     * Build sorting for query based on the column and order direction.
     *
     * @param cb         The CriteriaBuilder for constructing sorting.
     * @param task       The root task entity.
     * @param sortColumn The column to sort by.
     * @param ascending  Whether the sorting is ascending.
     * @return An Order object representing the sorting.
     */
    private Order buildOrder(CriteriaBuilder cb, Root<Task> task, String sortColumn, boolean ascending) {
        if (sortColumn.contains(".")) {
            String object = sortColumn.substring(0, sortColumn.indexOf("."));
            String property = sortColumn.substring(sortColumn.indexOf(".") + 1);
            Join<Task, Object> objectJoin = task.join(object, JoinType.LEFT);
            return ascending ? cb.asc(objectJoin.get(property)) : cb.desc(objectJoin.get(property));
        } else {
            return ascending ? cb.asc(task.get(sortColumn)) : cb.desc(task.get(sortColumn));
        }
    }
}
