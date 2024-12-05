package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.PriorityService;
import com.ffhs.jeetasks.service.StatusService;
import com.ffhs.jeetasks.service.TaskService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Named
@SessionScoped
public class TaskBean implements Serializable {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Inject
    private TaskService taskService;
    @Inject
    private PriorityService priorityService;
    @Inject
    private StatusService statusService;

    private TaskList currentlySelectedList;

    private Task taskEdit;

    private String newTaskTitle;
    private String newTaskDescription;
    private String newTaskDueDateString;
    private Long newTaskPriorityId;
    private Long newTaskStatusId;

    public List<Task> getTasks() {
        return taskService.findAllTasksByListId(currentlySelectedList != null ? currentlySelectedList.getListId() : null);
    }

    public void setCurrentList(TaskList currentList) {
        this.currentlySelectedList = currentList;
    }

    public void prepareForEdit(Task task) {
        taskEdit = task;
        if (task != null) {
            newTaskTitle = task.getTitle();
            newTaskDescription = task.getDescription();
            newTaskStatusId = task.getStatus() != null ? task.getStatus().getStatusId() : null;
            newTaskPriorityId = task.getPriority() != null ? task.getPriority().getPriorityId() : null;
            newTaskDueDateString = task.getDueDate() != null ? task.getDueDate().toLocalDateTime().format(formatter) : null;
        } else {
            newTaskTitle = "";
            newTaskDescription = "";
            newTaskStatusId = null;
            newTaskPriorityId = null;
            newTaskDueDateString = "";
        }
    }

    public void addTask() {
        Task task = new Task();
        setTaskData(task);
        task.setTaskList(currentlySelectedList);
        taskService.insertModel(task);
        newTaskTitle = "";
        newTaskDescription = "";
    }

    public void saveTask() {
        if (taskEdit == null) {
            addTask();
            return;
        }
        setTaskData(taskEdit);
        taskService.updateModel(taskEdit);
    }

    private void setTaskData(Task task) {
        task.setTitle(newTaskTitle);
        task.setDescription(newTaskDescription);
        if (newTaskDueDateString != null && !newTaskDueDateString.isEmpty()) {
            LocalDateTime localDateTime = LocalDateTime.parse(newTaskDueDateString, formatter);
            task.setDueDate(Timestamp.valueOf(localDateTime));
        }
        if (newTaskPriorityId != null) {
            task.setPriority(priorityService.findPriorityById(newTaskPriorityId));
        }
        if (newTaskStatusId != null) {
            task.setStatus(statusService.findStatusById(newTaskStatusId));
        }
        task.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    }
}