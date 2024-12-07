package com.ffhs.jeetasks.bean.task;

import com.ffhs.jeetasks.dto.TaskFormDTO;
import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.entity.Task;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.NotificationService;
import com.ffhs.jeetasks.service.TaskService;
import com.ffhs.jeetasks.service.UserService;
import com.ffhs.jeetasks.util.SessionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskBeanTest {

    @InjectMocks
    private TaskBean taskBean;

    @Mock
    private TaskService taskService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTasks_GroupedByStatus() {
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            // Arrange
            Task task1 = new Task();
            task1.setStatus(new Status());
            Task task2 = new Task();
            task2.setStatus(new Status());

            List<Task> tasks = Arrays.asList(task1, task2);

            User mockUser = new User();
            mockUser.setUserId(1L);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(mockUser);
            when(taskService.findAllTasksByListId(null, "taskId", true, TaskBean.TECHNICAL_LIST_TYPE.ALL_TASKS, 1L)).thenReturn(tasks);
            when(taskService.groupTasksByStatus(tasks)).thenReturn(Map.of(new Status(), tasks));

            // Act
            taskBean.toggleGroupByStatus(); // Enable grouping by status
            Map<Status, List<Task>> result = taskBean.getTasks();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(taskService, times(1)).groupTasksByStatus(tasks);
        }
    }

    @Test
    void testSetCurrentList() {
        // Arrange
        TaskList taskList = new TaskList();
        TaskBean.TECHNICAL_LIST_TYPE type = TaskBean.TECHNICAL_LIST_TYPE.CUSTOM_LIST;

        // Act
        taskBean.setCurrentList(taskList, type);

        // Assert
        assertEquals(taskList, taskBean.getCurrentlySelectedList());
        assertEquals(type, taskBean.getCurrentlySelectedListType());
    }

    @Test
    void testPrepareForEditByTaskId() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1L);
        TaskFormDTO taskFormDTO = new TaskFormDTO();
        taskFormDTO.setTitle("Sample Task");

        when(taskService.findTaskById(1L)).thenReturn(task);
        when(taskService.toFormDTO(task)).thenReturn(taskFormDTO);

        // Act
        taskBean.prepareForEdit(task);

        // Assert
        assertEquals(task, taskBean.getTaskEdit());
        assertNotNull(taskBean.getTaskForm());
        assertEquals("Sample Task", taskBean.getTaskForm().getTitle());
    }

    @Test
    void testSaveTask_NewTask() {
        // Arrange
        TaskFormDTO taskFormDTO = new TaskFormDTO();
        taskFormDTO.setTitle("New Task");
        taskFormDTO.setDescription("Task Description");

        taskBean.getTaskForm().setTitle("New Task");
        taskBean.getTaskForm().setDescription("Task Description");

        doAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setTitle(taskFormDTO.getTitle());
            task.setDescription(taskFormDTO.getDescription());
            return null;
        }).when(taskService).updateTaskFromDTO(any(Task.class), eq(taskFormDTO));

        // Act
        taskBean.saveTask();

        // Capture the arguments passed to updateTaskFromDTO
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskService, times(1)).updateTaskFromDTO(taskCaptor.capture(), eq(taskFormDTO));

        // Assert
        Task capturedTask = taskCaptor.getValue();
        assertNotNull(capturedTask);
        assertEquals("New Task", capturedTask.getTitle());
        assertEquals("Task Description", capturedTask.getDescription());

        verify(taskService, times(1)).insertModel(capturedTask);
    }

    @Test
    void testDeleteTask_Positive() {
        // Arrange
        Task task = new Task();
        taskBean.prepareForEdit(task);

        // Act
        taskBean.deleteTask();

        // Assert
        verify(taskService, times(1)).deleteModel(task);
    }

    @Test
    void testSortBy_ToggleAscending() {
        // Arrange
        String column = "title";

        // Act
        taskBean.sortBy(column);

        // Assert
        assertEquals("title", taskBean.sortColumn);
        assertTrue(taskBean.ascending);

        // Act - toggle direction
        taskBean.sortBy(column);

        // Assert
        assertFalse(taskBean.ascending);
    }
}
