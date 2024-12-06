package com.ffhs.jeetasks.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "notification_user_id", nullable = false)
    private User user;

    @Column(name = "notification_text", nullable = false)
    private String text;

    @Column (name = "notification_link")
    private String link;

    @Column (name = "notification_dismissed", nullable = false)
    private boolean dismissed;

    @Column(name = "notification_created_at")
    private Timestamp createdAt;
}
