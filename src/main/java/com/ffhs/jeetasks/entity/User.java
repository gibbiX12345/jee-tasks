package com.ffhs.jeetasks.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_user_id")
    private Long userId;

    @Column(name = "app_user_email", unique = true, nullable = false)
    private String email;

    @Column(name = "app_user_password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "app_user_created_at")
    private Timestamp createdAt;
}
