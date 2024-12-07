package com.ffhs.jeetasks.bean.task;

import com.ffhs.jeetasks.entity.Comment;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.CommentService;
import com.ffhs.jeetasks.util.SessionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentBeanTest {

    @Mock
    private CommentService commentService;

    @Mock
    private SessionUtils sessionUtils;

    @InjectMocks
    private CommentBean commentBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadCommentsForTask_Positive() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1L);
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());
        when(commentService.findAllCommentsByTaskId(1L)).thenReturn(comments);

        // Act
        commentBean.loadCommentsForTask(task);

        // Assert
        assertNotNull(commentBean.getCommentsForTask());
        assertEquals(2, commentBean.getCommentsForTask().size());
        assertEquals(task, commentBean.getCurrentTask());
    }

    @Test
    void testLoadCommentsForTask_TaskIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> commentBean.loadCommentsForTask(null));
    }

    @Test
    void testLoadCommentsForTask_NoComments() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1L);
        when(commentService.findAllCommentsByTaskId(1L)).thenReturn(Collections.emptyList());

        // Act
        commentBean.loadCommentsForTask(task);

        // Assert
        assertNotNull(commentBean.getCommentsForTask());
        assertTrue(commentBean.getCommentsForTask().isEmpty());
    }

    @Test
    void testAddComment_Positive() {
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            // Arrange
            Task task = new Task();
            task.setTaskId(1L);
            commentBean.setCurrentTask(task);
            commentBean.setNewCommentContent("New comment");

            User mockUser = new User();
            mockUser.setUserId(1L);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(mockUser);
            doNothing().when(commentService).insertModel(any(Comment.class));
            when(commentService.findAllCommentsByTaskId(1L)).thenReturn(Collections.singletonList(new Comment()));

            // Act
            commentBean.addComment();

            // Assert
            verify(commentService, times(1)).insertModel(any(Comment.class));
            verify(commentService, times(1)).findAllCommentsByTaskId(1L);
            assertEquals("", commentBean.getNewCommentContent());
        }
    }

    @Test
    void testAddComment_EmptyContent() {
        // Arrange
        commentBean.setNewCommentContent("   ");

        // Act
        commentBean.addComment();

        // Assert
        verifyNoInteractions(commentService);
    }

    @Test
    void testAddComment_NullContent() {
        // Arrange
        commentBean.setNewCommentContent(null);

        // Act
        commentBean.addComment();

        // Assert
        verifyNoInteractions(commentService);
    }

    @Test
    void testCreateComment_Positive() {
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            // Arrange
            Task task = new Task();
            task.setTaskId(1L);
            commentBean.setCurrentTask(task);
            User mockUser = new User();
            mockUser.setUserId(1L);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(mockUser);

            // Act
            Comment comment = commentBean.createComment("Test comment");

            // Assert
            assertNotNull(comment);
            assertEquals("Test comment", comment.getContent());
            assertEquals(task, comment.getTask());
            assertNotNull(comment.getCreatedAt());
        }
    }
}
