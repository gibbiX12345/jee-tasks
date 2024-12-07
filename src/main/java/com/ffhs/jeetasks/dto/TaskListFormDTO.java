package com.ffhs.jeetasks.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskListFormDTO implements Serializable {
    private String title;
    private String description;
}
