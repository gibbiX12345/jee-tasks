package com.ffhs.jeetasks.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO representing the form data for a task list.
 */
@Data
public class TaskListFormDTO implements Serializable {
    private String title;
    private String description;
}
