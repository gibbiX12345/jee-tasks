package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Status;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

/**
 * Service class for managing task statuses.
 * Provides methods to retrieve and manage status entities.
 */
@Stateless
public class StatusService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    /**
     * Retrieves all statuses from the database, ordered by their defined order.
     *
     * @return A list of {@link Status} entities.
     */
    public List<Status> findAllStatuses() {
        return entityManager.createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class).getResultList();
    }
}
