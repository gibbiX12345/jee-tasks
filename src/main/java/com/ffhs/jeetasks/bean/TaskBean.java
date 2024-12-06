package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.service.PriorityService;
import com.ffhs.jeetasks.service.StatusService;
import com.ffhs.jeetasks.service.TaskService;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Inject
    private UserService userService;

    private TaskList currentlySelectedList;

    private Task taskEdit;

    private String taskTitle;
    private String taskDescription;
    private String taskDueDateString;
    private Long taskAssigendUserId;
    private Long taskPriorityId;
    private Long taskStatusId;

    private boolean groupByStatus = false;

    private String sortColumn = "taskId";
    private boolean ascending = true;

    public Map<Status, List<Task>> getTasks() {
        List<Task> tasks = taskService.findAllTasksByListId(currentlySelectedList != null ? currentlySelectedList.getListId() : null, sortColumn, ascending);
        if (groupByStatus) {
            return tasks.stream()
                    .collect(Collectors.groupingBy(
                            task -> task.getStatus() != null ? task.getStatus() : getDefaultStatus(),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ))
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(entry -> entry.getKey().getOrder() != null ? entry.getKey().getOrder() : Integer.MAX_VALUE))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        }
        return tasks.stream().collect(Collectors.groupingBy(task -> getDefaultStatus(), LinkedHashMap::new, Collectors.toList()));
    }

    private Status getDefaultStatus() {
        Status defaultStatus = new Status();
        defaultStatus.setValue("No Status");
        defaultStatus.setOrder(Integer.MAX_VALUE);
        return defaultStatus;
    }

    public void setCurrentList(TaskList currentList) {
        this.currentlySelectedList = currentList;
    }

    public void prepareForEdit(Task task) {
        taskEdit = task;
        if (task != null) {
            taskTitle = task.getTitle();
            taskDescription = task.getDescription();
            taskAssigendUserId = task.getAssignedUser() != null ? task.getAssignedUser().getUserId() : null;
            taskStatusId = task.getStatus() != null ? task.getStatus().getStatusId() : null;
            taskPriorityId = task.getPriority() != null ? task.getPriority().getPriorityId() : null;
            taskDueDateString = task.getDueDate() != null ? task.getDueDate().toLocalDateTime().format(formatter) : null;
        } else {
            taskTitle = "";
            taskDescription = "";
            taskAssigendUserId = null;
            taskStatusId = null;
            taskPriorityId = null;
            taskDueDateString = "";
        }
    }

    public void addTask() {
        Task task = new Task();
        setTaskData(task);
        task.setTaskList(currentlySelectedList);
        taskService.insertModel(task);
        taskTitle = "";
        taskDescription = "";
    }

    public void saveTask() {
        if (taskEdit == null) {
            addTask();
            return;
        }
        setTaskData(taskEdit);
        taskService.updateModel(taskEdit);
    }

    public void deleteTask() {
        if (taskEdit != null) {
            taskService.deleteModel(taskEdit);
        }
    }

    private void setTaskData(Task task) {
        task.setTitle(taskTitle);
        task.setDescription(taskDescription);
        if (taskAssigendUserId != null) {
            task.setAssignedUser(userService.findUserById(taskAssigendUserId));
        }
        if (taskDueDateString != null && !taskDueDateString.isEmpty()) {
            LocalDateTime localDateTime = LocalDateTime.parse(taskDueDateString, formatter);
            task.setDueDate(Timestamp.valueOf(localDateTime));
        }
        if (taskPriorityId != null) {
            task.setPriority(priorityService.findPriorityById(taskPriorityId));
        }
        if (taskStatusId != null) {
            task.setStatus(statusService.findStatusById(taskStatusId));
        }
        task.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    }

    public void toggleGroupByStatus() {
        groupByStatus = !groupByStatus;
    }


    public void sortBy(String column) {
        if (sortColumn.equals(column)) {
            ascending = !ascending;
        } else {
            sortColumn = column;
            ascending = true;
        }
    }
}