package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Comment;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class CommentService {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<Comment> findAllComments() {
        return entityManager.createQuery("SELECT c FROM Comment c", Comment.class).getResultList();
    }
}
