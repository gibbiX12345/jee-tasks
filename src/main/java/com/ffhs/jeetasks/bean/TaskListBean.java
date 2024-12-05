package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.TaskListService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
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
    @Inject
    private LoginBean loginBean;

    private TaskList taskListEdit;
    private String editTaskListTitle;
    private String editTaskListDescription;

    private String newTaskListTitle;
    private String newTaskListDescription;

    public List<TaskList> getTaskLists() {
        return taskListService.findAllTaskListsForUser();
    }

    public void prepareForEdit(TaskList taskList) {
        taskListEdit = taskList;
        if (taskList != null) {
            editTaskListTitle = taskList.getTitle();
            editTaskListDescription = taskList.getDescription();
        }
    }

    public void addTaskList() {
        TaskList taskList = new TaskList();
        taskList.setTitle(newTaskListTitle);
        taskList.setDescription(newTaskListDescription);
        taskList.setUser(loginBean.getUser());
        taskList.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        taskListService.insertModel(taskList);
        newTaskListTitle = "";
        newTaskListDescription = "";
    }

    public void saveTaskList() {
        taskListEdit.setTitle(editTaskListTitle);
        taskListEdit.setDescription(editTaskListDescription);
        taskListService.updateModel(taskListEdit);
    }
}