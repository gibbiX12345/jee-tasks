package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.TaskListService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

import java.util.List;

@Data
@Named
@RequestScoped
public class TaskListBean {

    @Inject
    private TaskListService taskListService;
    @Inject
    private LoginBean loginBean;

    private String newTaskListTitle;

    public List<TaskList> getTaskLists() {
        return taskListService.findAllTaskLists();
    }

    public void addTaskList() {
        TaskList taskList = new TaskList();
        taskList.setTitle(newTaskListTitle);
        taskList.setUser(loginBean.getUser());
        taskListService.addTaskList(taskList);
    }
}