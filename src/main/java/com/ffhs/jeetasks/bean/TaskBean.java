package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.service.TaskService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

import java.util.List;

@Named
@RequestScoped
public class TaskBean {

    @Inject
    private TaskService taskService;

    @Getter
    private List<Task> tasks;

    @Getter
    private Long currentListId;

    public void initBean() {
        if (tasks == null) {
            tasks = taskService.findAllTasksByListId(currentListId);
        }
    }

    public String loadTasks(Long listId) {
        this.currentListId = listId;
        tasks = taskService.findAllTasksByListId(listId);
        return null;
    }
}