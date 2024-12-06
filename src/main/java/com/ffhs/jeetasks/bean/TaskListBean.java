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
        taskList.setUser(loginBean.getUser());
        taskList.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        taskListService.insertModel(taskList);
        taskListTitle = "";
        taskListDescription = "";
    }

    public void saveTaskList() {
        if (taskListEdit == null) {
            addTaskList();
            return;
        }
        setTaskListData(taskListEdit);
        taskListService.updateModel(taskListEdit);
    }

    private void setTaskListData(TaskList taskList) {
        taskList.setTitle(taskListTitle);
        taskList.setDescription(taskListDescription);
    }
}