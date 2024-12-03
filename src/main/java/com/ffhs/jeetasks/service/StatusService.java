package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Status;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class StatusService {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<Status> findAllStatuses() {
        return entityManager.createQuery("SELECT s FROM Status s", Status.class).getResultList();
    }
}
