package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.entity.Status;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

@Stateless
public class StatusService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<Status> findAllStatuses() {
        return entityManager.createQuery("SELECT s FROM Status s", Status.class).getResultList();
    }

    public Status findStatusById(Long id) {
        return entityManager.createQuery("SELECT p FROM Status p WHERE p.statusId = :statusId", Status.class)
                .setParameter("statusId", id)
                .getSingleResult();
    }
}
