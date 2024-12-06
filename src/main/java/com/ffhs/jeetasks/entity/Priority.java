package com.ffhs.jeetasks.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "priority")
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id")
    private Long priorityId;

    @Column(name = "priority_level", nullable = false)
    private String level;

    @Column(name = "priority_order", nullable = false)
    private Integer order;
}
