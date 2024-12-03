package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.TaskListService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class TaskListBean {

    @Inject
    private TaskListService taskListService;

    public List<TaskList> getTaskLists() {
        return taskListService.findAllTaskLists();
    }
}