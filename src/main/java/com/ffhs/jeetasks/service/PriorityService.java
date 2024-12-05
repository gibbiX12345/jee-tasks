package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Priority;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

@Stateless
public class PriorityService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<Priority> findAllPriorities() {
        return entityManager.createQuery("SELECT p FROM Priority p", Priority.class).getResultList();
    }

    public Priority findPriorityById(Long id) {
        return entityManager.createQuery("SELECT p FROM Priority p WHERE p.priorityId = :priorityId", Priority.class)
                .setParameter("priorityId", id)
                .getSingleResult();
    }
}
