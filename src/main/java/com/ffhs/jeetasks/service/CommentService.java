package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Comment;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

/**
 * Service class responsible for managing Comment entities.
 */
@Stateless
public class CommentService implements Serializable {

    @PersistenceContext(unitName = "jee-tasks-pu")
    private EntityManager entityManager;

    /**
     * Retrieves all comments associated with a specific task, ordered by creation date.
     *
     * @param taskId The ID of the task whose comments are to be retrieved.
     * @return A list of comments for the specified task.
     */
    public List<Comment> findAllCommentsByTaskId(Long taskId) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.task.taskId = :taskId ORDER BY c.createdAt", Comment.class)
                .setParameter("taskId", taskId)
                .getResultList();
    }

    /**
     * Persists a new comment into the database.
     *
     * @param comment The Comment entity to be saved.
     */
    public void insertModel(Comment comment) {
        entityManager.persist(comment);
    }
}
