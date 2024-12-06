package com.ffhs.jeetasks.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "comment_task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "comment_user_id", nullable = false)
    private User user;

    @Column(name = "comment_content", nullable = false)
    private String content;

    @Column(name = "comment_created_at")
    private Timestamp createdAt;


    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "-";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return formatter.format(createdAt);
    }
}
