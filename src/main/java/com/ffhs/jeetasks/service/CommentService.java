package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Comment;
import com.ffhs.jeetasks.entity.Task;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

@Stateless
public class CommentService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    public List<Comment> findAllComments() {
        return entityManager.createQuery("SELECT c FROM Comment c", Comment.class).getResultList();
    }

    public List<Comment> findAllCommentsByTaskId(Long taskId) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.task.taskId = :taskId ORDER BY c.createdAt", Comment.class)
                .setParameter("taskId", taskId)
                .getResultList();
    }

    public void insertModel(Comment comment) {
        entityManager.persist(comment);
    }

}
