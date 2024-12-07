package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.dto.TaskListFormDTO;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskListServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TaskListService taskListService;

    @Mock
    private TypedQuery<TaskList> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllTaskListsForUser_Positive() {
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            // Arrange
            TaskList list1 = new TaskList();
            list1.setTitle("List 1");
            list1.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            TaskList list2 = new TaskList();
            list2.setTitle("List 2");
            list2.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            List<TaskList> taskLists = Arrays.asList(list1, list2);

            User mockUser = new User();
            mockUser.setUserId(1L);
            mockedSessionUtils.when(SessionUtils::isLoggedIn).thenReturn(true);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(mockUser);

            when(entityManager.createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId ORDER BY l.createdAt", TaskList.class)).thenReturn(query);
            when(query.setParameter("userId", 1L)).thenReturn(query);
            when(query.getResultList()).thenReturn(taskLists);

            // Act
            List<TaskList> result = taskListService.findAllTaskListsForUser();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("List 1", result.get(0).getTitle());
            assertEquals("List 2", result.get(1).getTitle());
            verify(entityManager, times(1)).createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId ORDER BY l.createdAt", TaskList.class);
            verify(query, times(1)).setParameter("userId", 1L);
            verify(query, times(1)).getResultList();
        }
    }

    @Test
    void testFindAllTaskListsForUser_NotLoggedIn() {
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            // Arrange
            mockedSessionUtils.when(SessionUtils::isLoggedIn).thenReturn(false);

            // Act
            List<TaskList> result = taskListService.findAllTaskListsForUser();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(entityManager, never()).createQuery(anyString(), eq(TaskList.class));
        }
    }

    @Test
    void testFindAllTaskListsForUser_EmptyList() {
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            // Arrange
            User mockUser = new User();
            mockUser.setUserId(1L);
            mockedSessionUtils.when(SessionUtils::isLoggedIn).thenReturn(true);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(mockUser);

            when(entityManager.createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId ORDER BY l.createdAt", TaskList.class)).thenReturn(query);
            when(query.setParameter("userId", 1L)).thenReturn(query);
            when(query.getResultList()).thenReturn(Collections.emptyList());

            // Act
            List<TaskList> result = taskListService.findAllTaskListsForUser();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(entityManager, times(1)).createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId ORDER BY l.createdAt", TaskList.class);
            verify(query, times(1)).setParameter("userId", 1L);
            verify(query, times(1)).getResultList();
        }
    }

    @Test
    void testFindAllTaskListsForUser_Exception() {
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            // Arrange
            User mockUser = new User();
            mockUser.setUserId(1L);
            mockedSessionUtils.when(SessionUtils::isLoggedIn).thenReturn(true);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(mockUser);

            when(entityManager.createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId ORDER BY l.createdAt", TaskList.class)).thenReturn(query);
            when(query.setParameter("userId", 1L)).thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, taskListService::findAllTaskListsForUser);
            assertEquals("Database error", exception.getMessage());
            verify(entityManager, times(1)).createQuery("SELECT l FROM TaskList l WHERE l.user.userId = :userId ORDER BY l.createdAt", TaskList.class);
            verify(query, times(1)).setParameter("userId", 1L);
        }
    }

    @Test
    void testToFormDTO() {
        // Arrange
        TaskList taskList = new TaskList();
        taskList.setTitle("Sample Title");
        taskList.setDescription("Sample Description");

        // Act
        TaskListFormDTO result = taskListService.toFormDTO(taskList);

        // Assert
        assertNotNull(result);
        assertEquals("Sample Title", result.getTitle());
        assertEquals("Sample Description", result.getDescription());
    }

    @Test
    void testUpdateTaskListFromDTO() {
        // Arrange
        TaskList taskList = new TaskList();
        TaskListFormDTO dto = new TaskListFormDTO();
        dto.setTitle("Updated Title");
        dto.setDescription("Updated Description");

        // Act
        taskListService.updateTaskListFromDTO(taskList, dto);

        // Assert
        assertEquals("Updated Title", taskList.getTitle());
        assertEquals("Updated Description", taskList.getDescription());
    }

    @Test
    void testInsertModel() {
        // Arrange
        TaskList taskList = new TaskList();

        // Act
        taskListService.insertModel(taskList);

        // Assert
        verify(entityManager, times(1)).persist(taskList);
    }

    @Test
    void testUpdateModel() {
        // Arrange
        TaskList taskList = new TaskList();

        // Act
        taskListService.updateModel(taskList);

        // Assert
        verify(entityManager, times(1)).merge(taskList);
    }
}
