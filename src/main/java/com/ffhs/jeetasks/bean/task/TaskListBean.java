package com.ffhs.jeetasks.bean.task;

import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.TaskListService;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Bean responsible for managing task lists within the session scope.
 */
@Named
@SessionScoped
public class TaskListBean implements Serializable {

    @Inject
    private TaskListService taskListService;

    @Getter
    @Setter
    private TaskList taskListEdit;

    @Getter
    @Setter
    private String taskListTitle;

    @Getter
    @Setter
    private String taskListDescription;

    /**
     * Retrieves all task lists associated with the logged-in user.
     *
     * @return List of task lists.
     */
    public List<TaskList> getTaskLists() {
        return taskListService.findAllTaskListsForUser();
    }

    /**
     * Prepares the bean for editing or creating a new task list.
     *
     * @param taskList The task list to edit, or null for a new task list.
     */
    public void prepareForEdit(TaskList taskList) {
        taskListEdit = taskList;
        if (taskList != null) {
            taskListTitle = taskList.getTitle();
            taskListDescription = taskList.getDescription();
        } else {
            resetForm();
        }
    }

    /**
     * Creates and persists a new task list.
     */
    public void addTaskList() {
        TaskList taskList = new TaskList();
        populateTaskListData(taskList);
        taskList.setUser(SessionUtils.getLoggedInUser());
        taskList.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        taskListService.insertModel(taskList);
        resetForm();
    }

    /**
     * Saves the currently edited task list or creates a new one if none is being edited.
     *
     * @return Navigation outcome, or null to stay on the same page.
     */
    public String saveTaskList() {
        if (!isValid()) {
            return null;
        }
        if (taskListEdit == null) {
            addTaskList();
        } else {
            populateTaskListData(taskListEdit);
            taskListService.updateModel(taskListEdit);
        }
        return null;
    }

    /**
     * Validates the task list data.
     *
     * @return True if valid, false otherwise.
     */
    private boolean isValid() {
        if (taskListTitle == null || taskListTitle.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save unsuccessful", "List Name can't be empty"));
            return false;
        }
        return true;
    }

    /**
     * Populates the given task list with data from the form fields.
     *
     * @param taskList The task list to populate.
     */
    private void populateTaskListData(TaskList taskList) {
        taskList.setTitle(taskListTitle);
        taskList.setDescription(taskListDescription);
    }

    /**
     * Resets the form fields for creating or editing a task list.
     */
    private void resetForm() {
        taskListEdit = null;
        taskListTitle = "";
        taskListDescription = "";
    }
}
