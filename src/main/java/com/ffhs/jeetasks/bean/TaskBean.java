package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.dto.TaskFormDTO;
import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.NotificationService;
import com.ffhs.jeetasks.service.TaskService;
import com.ffhs.jeetasks.service.UserService;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TaskBean is a managed bean for handling task-related operations in a web application.
 * It manages the state and interactions for tasks, including creation, editing, grouping, and notifications.
 */
@Named
@SessionScoped
public class TaskBean implements Serializable {

    @Inject
    private TaskService taskService;

    @Inject
    private NotificationService notificationService;

    @Inject
    private UserService userService;

    @Inject
    private PushBean pushBean;

    @Getter
    private TaskList currentlySelectedList;

    @Getter
    private TECHNICAL_LIST_TYPE currentlySelectedListType = TECHNICAL_LIST_TYPE.ALL_TASKS;

    @Getter
    private Task taskEdit;

    @Getter
    private TaskFormDTO taskForm = new TaskFormDTO();

    @Getter
    private boolean groupByStatus = false;

    private String sortColumn = "taskId";
    private boolean ascending = true;

    public enum TECHNICAL_LIST_TYPE {CUSTOM_LIST, ALL_TASKS, MY_ASSIGNED_TASKS}

    /**
     * Retrieves tasks grouped by status or as a single list based on user preference.
     *
     * @return A map of tasks grouped by status.
     */
    public Map<Status, List<Task>> getTasks() {
        List<Task> tasks = taskService.findAllTasksByListId(
                currentlySelectedList != null ? currentlySelectedList.getListId() : null,
                sortColumn, ascending, currentlySelectedListType, SessionUtils.getLoggedInUser().getUserId()
        );

        return groupByStatus
                ? taskService.groupTasksByStatus(tasks)
                : taskService.groupTasksWithDefaultStatus(tasks, taskService.getDefaultStatus());
    }

    /**
     * Sets the current task list and type for filtering tasks.
     *
     * @param currentList The task list to set.
     * @param type        The type of the list (e.g., custom, all tasks, assigned tasks).
     */
    public void setCurrentList(TaskList currentList, TECHNICAL_LIST_TYPE type) {
        this.currentlySelectedList = currentList;
        this.currentlySelectedListType = type;
    }

    /**
     * Prepares a task for editing based on its ID retrieved from the request parameters.
     */
    public void prepareForEditByTaskId() {
        Long taskId = Long.parseLong(getRequestParam("taskId"));
        prepareForEdit(taskService.findTaskById(taskId));
    }

    /**
     * Prepares a task for editing by converting it to a TaskFormDTO.
     *
     * @param task The task to prepare.
     */
    public void prepareForEdit(Task task) {
        taskEdit = task;
        taskForm = task != null ? taskService.toFormDTO(task) : new TaskFormDTO();
    }

    /**
     * Saves a task, creating a new one or updating an existing one.
     * Sends notifications if the assignee changes.
     */
    public void saveTask() {
        Task task = taskEdit != null ? taskEdit : new Task();
        boolean isAssigneeChanged = isAssigneeChanged(task);
        boolean isNotSelfAssignment = isNotSelfAssignment();

        taskService.updateTaskFromDTO(task, taskForm);

        if (taskEdit == null) {
            task.setTaskList(currentlySelectedList);
            taskService.insertModel(task);
            sendNotificationIfNeeded(task, isAssigneeChanged, isNotSelfAssignment, true);
        } else {
            taskService.updateModel(task);
            sendNotificationIfNeeded(task, isAssigneeChanged, isNotSelfAssignment, false);
        }

        resetForm();
    }

    /**
     * Deletes the currently selected task.
     */
    public void deleteTask() {
        if (taskEdit != null) {
            taskService.deleteModel(taskEdit);
        }
    }

    /**
     * Toggles the grouping of tasks by their status.
     */
    public void toggleGroupByStatus() {
        groupByStatus = !groupByStatus;
    }

    /**
     * Sorts tasks by a given column, toggling the sort direction if the column is the same.
     *
     * @param column The column to sort by.
     */
    public void sortBy(String column) {
        if (sortColumn.equals(column)) {
            ascending = !ascending;
        } else {
            sortColumn = column;
            ascending = true;
        }
    }

    /**
     * Checks if the task's assignee has been changed.
     *
     * @param task The task to check.
     * @return True if the assignee has changed, false otherwise.
     */
    private boolean isAssigneeChanged(Task task) {
        return taskForm.getAssignedUserId() != null &&
                (task == null || task.getAssignedUser() == null ||
                        !taskForm.getAssignedUserId().equals(task.getAssignedUser().getUserId()));
    }

    /**
     * Checks if the task is assigned to a user other than the current user.
     *
     * @return True if the assignee is not the current user, false otherwise.
     */
    private boolean isNotSelfAssignment() {
        return taskForm.getAssignedUserId() != null &&
                !taskForm.getAssignedUserId().equals(SessionUtils.getLoggedInUser().getUserId());
    }

    /**
     * Sends a notification if the assignee is changed and it is not a self-assignment.
     *
     * @param task                The task for which to send a notification.
     * @param isAssigneeChanged   Whether the assignee has changed.
     * @param isNotSelfAssignment Whether the assignment is not to the current user.
     * @param isNew               Whether the task is new.
     */
    private void sendNotificationIfNeeded(Task task, boolean isAssigneeChanged, boolean isNotSelfAssignment, boolean isNew) {
        if (isAssigneeChanged && isNotSelfAssignment) {
            String notificationText = isNew
                    ? "Task '" + task.getTitle() + "' was assigned to you by user " + SessionUtils.getLoggedInUser().getEmail()
                    : "Task '" + task.getTitle() + "' was reassigned to you by user " + SessionUtils.getLoggedInUser().getEmail();
            String notificationLink = generateTaskLink(task);
            notificationService.createNotification(notificationText, userService.findUserById(taskForm.getAssignedUserId()), notificationLink);
            pushBean.sendPushMessage("notification updated");
        }
    }

    /**
     * Generates a link to a task.
     *
     * @param task The task for which to generate the link.
     * @return A URL pointing to the task.
     */
    private String generateTaskLink(Task task) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                request.getContextPath() + "/index.xhtml?taskId=" + task.getTaskId();
    }

    /**
     * Retrieves a request parameter value by name.
     *
     * @param param The name of the parameter.
     * @return The value of the parameter.
     */
    private String getRequestParam(String param) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(param);
    }

    /**
     * Resets the task editing form and clears the current task selection.
     */
    private void resetForm() {
        taskEdit = null;
        taskForm = new TaskFormDTO();
    }
}
