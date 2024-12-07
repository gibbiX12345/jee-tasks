package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.dto.TaskFormDTO;
import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.entity.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TypedQuery<Task> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindTaskById_Positive() {
        // Arrange
        Task mockTask = new Task();
        mockTask.setTaskId(1L);
        mockTask.setTitle("Test Task");
        when(entityManager.find(Task.class, 1L)).thenReturn(mockTask);

        // Act
        Task result = taskService.findTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getTaskId());
        assertEquals("Test Task", result.getTitle());
        verify(entityManager, times(1)).find(Task.class, 1L);
    }

    @Test
    void testFindTaskById_Null() {
        // Arrange
        when(entityManager.find(Task.class, 1L)).thenReturn(null);

        // Act
        Task result = taskService.findTaskById(1L);

        // Assert
        assertNull(result);
        verify(entityManager, times(1)).find(Task.class, 1L);
    }

    @Test
    void testInsertModel() {
        // Arrange
        Task task = new Task();

        // Act
        taskService.insertModel(task);

        // Assert
        verify(entityManager, times(1)).persist(task);
    }

    @Test
    void testUpdateModel() {
        // Arrange
        Task task = new Task();

        // Act
        taskService.updateModel(task);

        // Assert
        verify(entityManager, times(1)).merge(task);
    }

    @Test
    void testDeleteModel() {
        // Arrange
        Task task = new Task();
        Task mergedTask = new Task();
        when(entityManager.merge(task)).thenReturn(mergedTask);

        // Act
        taskService.deleteModel(task);

        // Assert
        verify(entityManager, times(1)).merge(task);
        verify(entityManager, times(1)).remove(mergedTask);
    }

    @Test
    void testToFormDTO() {
        // Arrange
        Task task = new Task();
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setDueDate(Timestamp.valueOf(LocalDateTime.now()));

        // Act
        TaskFormDTO dto = taskService.toFormDTO(task);

        // Assert
        assertNotNull(dto);
        assertEquals("Task Title", dto.getTitle());
        assertEquals("Task Description", dto.getDescription());
    }

    @Test
    void testUpdateTaskFromDTO() {
        // Arrange
        Task task = new Task();
        TaskFormDTO dto = new TaskFormDTO();
        dto.setTitle("Updated Title");
        dto.setDescription("Updated Description");

        // Act
        taskService.updateTaskFromDTO(task, dto);

        // Assert
        assertEquals("Updated Title", task.getTitle());
        assertEquals("Updated Description", task.getDescription());
    }

    @Test
    void testGroupTasksByStatus() {
        // Arrange
        Status status1 = new Status();
        status1.setValue("Status 1");

        Status status2 = new Status();
        status2.setValue("Status 2");

        Task task1 = new Task();
        task1.setStatus(status1);

        Task task2 = new Task();
        task2.setStatus(status2);

        Task task3 = new Task();
        task3.setStatus(status1);

        List<Task> tasks = Arrays.asList(task1, task2, task3);

        // Act
        Map<Status, List<Task>> groupedTasks = taskService.groupTasksByStatus(tasks);

        // Assert
        assertNotNull(groupedTasks);
        assertEquals(2, groupedTasks.get(status1).size());
        assertEquals(1, groupedTasks.get(status2).size());
    }

    @Test
    void testGetDefaultStatus() {
        // Act
        Status defaultStatus = taskService.getDefaultStatus();

        // Assert
        assertNotNull(defaultStatus);
        assertEquals("No Status", defaultStatus.getValue());
    }
}
