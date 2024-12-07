package com.ffhs.jeetasks.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskFormDTO implements Serializable {
    private String title;
    private String description;
    private String dueDateString;
    private Long assignedUserId;
    private Long priorityId;
    private Long statusId;
}
