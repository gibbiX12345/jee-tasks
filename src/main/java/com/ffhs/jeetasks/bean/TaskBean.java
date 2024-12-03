package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.service.TaskService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class TaskBean {

    @Inject
    private TaskService taskService;

    public List<Task> getTasks() {
        return taskService.findAllTasksForUser();
    }
}