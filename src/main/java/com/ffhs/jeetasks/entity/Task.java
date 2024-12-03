package com.ffhs.jeetasks.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @ManyToOne
    @JoinColumn(name = "task_list_id", nullable = false)
    private TaskList taskList;

    @ManyToOne
    @JoinColumn(name = "task_status_id", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "task_priority_id", nullable = false)
    private Priority priority;

    @Column(name = "task_title", nullable = false)
    private String title;

    @Column(name = "task_description")
    private String description;

    @Column(name = "task_due_date")
    private Timestamp dueDate;

    @Column(name = "task_created_at")
    private Timestamp createdAt;
}
