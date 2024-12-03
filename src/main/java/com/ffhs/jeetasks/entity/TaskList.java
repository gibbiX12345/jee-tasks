package com.ffhs.jeetasks.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "task_list")
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_list_id")
    private Long listId;

    @ManyToOne
    @JoinColumn(name = "task_list_user_id", nullable = false)
    private User user;

    @Column(name = "task_list_title", nullable = false)
    private String title;

    @Column (name = "task_list_description")
    private String description;

    @Column(name = "task_list_created_at")
    private Timestamp createdAt;
}
