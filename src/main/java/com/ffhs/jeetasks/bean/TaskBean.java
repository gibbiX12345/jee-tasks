package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.dto.TaskFormDTO;
import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.NotificationService;
import com.ffhs.jeetasks.service.TaskService;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    @Inject
    private LoginBean loginBean;

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

    public Map<Status, List<Task>> getTasks() {
        List<Task> tasks = taskService.findAllTasksByListId(
                currentlySelectedList != null ? currentlySelectedList.getListId() : null,
                sortColumn, ascending, currentlySelectedListType, loginBean.getUser().getUserId()
        );

        return groupByStatus
                ? taskService.groupTasksByStatus(tasks)
                : taskService.groupTasksWithDefaultStatus(tasks, taskService.getDefaultStatus());
    }

    public void setCurrentList(TaskList currentList, TECHNICAL_LIST_TYPE type) {
        this.currentlySelectedList = currentList;
        this.currentlySelectedListType = type;
    }

    public void prepareForEditByTaskId() {
        Long taskId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("taskId"));
        prepareForEdit(taskService.findTaskById(taskId));
    }

    public void prepareForEdit(Task task) {
        taskEdit = task;
        taskForm = task != null ? taskService.toFormDTO(task) : new TaskFormDTO();
    }

    public void saveTask() {
        Task task = taskEdit != null ? taskEdit : new Task();
        boolean isAssigneeChanged = taskForm.getAssignedUserId() != null &&
                (taskEdit == null || taskEdit.getAssignedUser() == null ||
                        !taskForm.getAssignedUserId().equals(taskEdit.getAssignedUser().getUserId()));
        boolean isNotSelfAssignment = taskForm.getAssignedUserId() != null &&
                !taskForm.getAssignedUserId().equals(loginBean.getUser().getUserId());

        taskService.updateTaskFromDTO(task, taskForm);

        if (taskEdit == null) {
            task.setTaskList(currentlySelectedList);
            taskService.insertModel(task);
            if (isAssigneeChanged && isNotSelfAssignment) {
                String notificationText = "Task '" + task.getTitle() + "' was assigned to you by user " + loginBean.getUser().getEmail();
                String notificationLink = generateTaskLink(task);
                notificationService.createNotification(notificationText, userService.findUserById(taskForm.getAssignedUserId()), notificationLink);
                pushBean.sendPushMessage("notification created");
            }
        } else {
            taskService.updateModel(task);
            if (isAssigneeChanged && isNotSelfAssignment) {
                String notificationText = "Task '" + task.getTitle() + "' was reassigned to you by user " + loginBean.getUser().getEmail();
                String notificationLink = generateTaskLink(task);
                notificationService.createNotification(notificationText, userService.findUserById(taskForm.getAssignedUserId()), notificationLink);
                pushBean.sendPushMessage("notification updated");
            }
        }

        taskEdit = null;
        taskForm = new TaskFormDTO();
    }

    public void deleteTask() {
        if (taskEdit != null) {
            taskService.deleteModel(taskEdit);
        }
    }

    public void toggleGroupByStatus() {
        groupByStatus = !groupByStatus;
    }

    public void sortBy(String column) {
        if (sortColumn.equals(column)) {
            ascending = !ascending;
        } else {
            sortColumn = column;
            ascending = true;
        }
    }

    private Status getDefaultStatus() {
        return taskService.getDefaultStatus();
    }

    private String generateTaskLink(Task task) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                request.getContextPath() + "/index.xhtml?taskId=" + task.getTaskId();
    }

}