package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Priority;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

/**
 * Service class for managing task priorities.
 * Provides methods to retrieve and manage priority entities.
 */
@Stateless
public class PriorityService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    /**
     * Retrieves all priorities from the database, ordered by their defined order.
     *
     * @return A list of {@link Priority} entities.
     */
    public List<Priority> findAllPriorities() {
        return entityManager.createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class).getResultList();
    }
}
