package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Priority;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class PriorityService {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<Priority> findAllPriorities() {
        return entityManager.createQuery("SELECT c FROM Priority c", Priority.class).getResultList();
    }
}
