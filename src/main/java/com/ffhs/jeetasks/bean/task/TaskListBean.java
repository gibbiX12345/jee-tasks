package com.ffhs.jeetasks.bean.task;

import com.ffhs.jeetasks.dto.TaskListFormDTO;
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
    private TaskListFormDTO taskListForm = new TaskListFormDTO();

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
        taskListForm = taskList != null ? taskListService.toFormDTO(taskList) : new TaskListFormDTO();
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
        TaskList taskList = taskListEdit != null ? taskListEdit : new TaskList();

        taskListService.updateTaskListFromDTO(taskList, taskListForm);
        if (taskListEdit == null) {
            taskList.setUser(SessionUtils.getLoggedInUser());
            taskListService.insertModel(taskList);
        } else {
            taskListService.updateModel(taskList);
        }
        return null;
    }

    /**
     * Validates the task list data.
     *
     * @return True if valid, false otherwise.
     */
    private boolean isValid() {
        if (taskListForm.getTitle() == null || taskListForm.getTitle().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save unsuccessful", "List Name can't be empty"));
            return false;
        }
        return true;
    }
}
