package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Comment> query;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllCommentsByTaskId() {
        // Arrange
        Long taskId = 1L;
        Comment comment1 = new Comment();
        comment1.setContent("First comment");
        Comment comment2 = new Comment();
        comment2.setContent("Second comment");

        List<Comment> mockComments = Arrays.asList(comment1, comment2);

        when(entityManager.createQuery("SELECT c FROM Comment c WHERE c.task.taskId = :taskId ORDER BY c.createdAt", Comment.class))
                .thenReturn(query);
        when(query.setParameter("taskId", taskId)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockComments);

        // Act
        List<Comment> result = commentService.findAllCommentsByTaskId(taskId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("First comment", result.get(0).getContent());
        assertEquals("Second comment", result.get(1).getContent());

        verify(entityManager, times(1)).createQuery(anyString(), eq(Comment.class));
        verify(query, times(1)).setParameter("taskId", taskId);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindAllCommentsByTaskId_NoComments() {
        // Arrange
        Long taskId = 1L;

        when(entityManager.createQuery("SELECT c FROM Comment c WHERE c.task.taskId = :taskId ORDER BY c.createdAt", Comment.class))
                .thenReturn(query);
        when(query.setParameter("taskId", taskId)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<Comment> result = commentService.findAllCommentsByTaskId(taskId);

        // Assert
        assertTrue(result.isEmpty());

        verify(entityManager, times(1)).createQuery(anyString(), eq(Comment.class));
        verify(query, times(1)).setParameter("taskId", taskId);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindAllCommentsByTaskId_InvalidTaskId() {
        // Arrange
        Long invalidTaskId = -1L;

        when(entityManager.createQuery("SELECT c FROM Comment c WHERE c.task.taskId = :taskId ORDER BY c.createdAt", Comment.class))
                .thenReturn(query);
        when(query.setParameter("taskId", invalidTaskId)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<Comment> result = commentService.findAllCommentsByTaskId(invalidTaskId);

        // Assert
        assertTrue(result.isEmpty());

        verify(entityManager, times(1)).createQuery(anyString(), eq(Comment.class));
        verify(query, times(1)).setParameter("taskId", invalidTaskId);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testInsertModel() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("New comment");

        // Act
        commentService.insertModel(comment);

        // Assert
        verify(entityManager, times(1)).persist(comment);
    }

    @Test
    void testInsertModel_NullComment() {
        // Act
        commentService.insertModel(null);

        // Assert
        verify(entityManager, times(1)).persist(null);
    }
}
