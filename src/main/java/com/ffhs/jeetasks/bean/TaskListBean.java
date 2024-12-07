package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.TaskListService;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Named
@SessionScoped
public class TaskListBean implements Serializable {

    @Inject
    private TaskListService taskListService;

    private TaskList taskListEdit;
    private String taskListTitle;
    private String taskListDescription;

    public List<TaskList> getTaskLists() {
        return taskListService.findAllTaskListsForUser();
    }

    public void prepareForEdit(TaskList taskList) {
        taskListEdit = taskList;
        if (taskList != null) {
            taskListTitle = taskList.getTitle();
            taskListDescription = taskList.getDescription();
        } else {
            taskListTitle = "";
            taskListDescription = "";
        }
    }

    public void addTaskList() {
        TaskList taskList = new TaskList();
        setTaskListData(taskList);
        taskList.setUser(SessionUtils.getLoggedInUser());
        taskList.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        taskListService.insertModel(taskList);
        taskListTitle = "";
        taskListDescription = "";
    }

    public String saveTaskList() {
        if (!isValid()) {
            return null;
        }
        if (taskListEdit == null) {
            addTaskList();
            return null;
        }
        setTaskListData(taskListEdit);
        taskListService.updateModel(taskListEdit);
        return null;
    }

    private boolean isValid() {
        boolean isValid = true;
        if (taskListTitle == null || taskListTitle.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save unsuccessful", "List Name can't be empty"));
            isValid = false;
        }
        return isValid;
    }

    private void setTaskListData(TaskList taskList) {
        taskList.setTitle(taskListTitle);
        taskList.setDescription(taskListDescription);
    }
}