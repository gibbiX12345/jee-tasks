package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.TaskList;
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
    private TaskList currentList;

    public void initBean() {
        if (tasks == null) {
            tasks = taskService.findAllTasksByListId(currentList != null ? currentList.getListId() : null);
        }
    }

    public String loadTasks(TaskList listId) {
        this.currentList = listId;
        tasks = taskService.findAllTasksByListId(listId != null ? listId.getListId() : null);
        return null;
    }
}