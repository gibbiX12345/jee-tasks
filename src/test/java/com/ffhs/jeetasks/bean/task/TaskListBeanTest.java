package com.ffhs.jeetasks.bean.task;

import com.ffhs.jeetasks.dto.TaskListFormDTO;
import com.ffhs.jeetasks.entity.TaskList;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.TaskListService;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskListBeanTest {

    @InjectMocks
    private TaskListBean taskListBean;

    @Mock
    private TaskListService taskListService;

    private FacesContext mockedFacesContext;
    private MockedStatic<FacesContext> mockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock FacesContext.getCurrentInstance()
        mockedFacesContext = mock(FacesContext.class, RETURNS_DEEP_STUBS);
        mockedStatic = Mockito.mockStatic(FacesContext.class);
        mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(mockedFacesContext);
    }

    @AfterEach
    void tearDown() {
        // Close the mocked static context
        if (mockedStatic != null) {
            mockedStatic.close();
        }
    }

    @Test
    void testGetTaskLists_Positive() {
        // Arrange
        List<TaskList> taskLists = Arrays.asList(new TaskList(), new TaskList());
        when(taskListService.findAllTaskListsForUser()).thenReturn(taskLists);

        // Act
        List<TaskList> result = taskListBean.getTaskLists();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskListService, times(1)).findAllTaskListsForUser();
    }

    @Test
    void testGetTaskLists_EmptyList() {
        // Arrange
        when(taskListService.findAllTaskListsForUser()).thenReturn(Collections.emptyList());

        // Act
        List<TaskList> result = taskListBean.getTaskLists();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testPrepareForEdit_EditExistingTaskList() {
        // Arrange
        TaskList taskList = new TaskList();
        taskList.setTitle("Test List");
        TaskListFormDTO formDTO = new TaskListFormDTO();
        formDTO.setTitle("Test List");
        formDTO.setDescription("Description");
        when(taskListService.toFormDTO(taskList)).thenReturn(formDTO);

        // Act
        taskListBean.prepareForEdit(taskList);

        // Assert
        assertEquals(taskList, taskListBean.getTaskListEdit());
        assertEquals("Test List", taskListBean.getTaskListForm().getTitle());
    }

    @Test
    void testPrepareForEdit_CreateNewTaskList() {
        // Act
        taskListBean.prepareForEdit(null);

        // Assert
        assertNull(taskListBean.getTaskListEdit());
        assertNotNull(taskListBean.getTaskListForm());
        assertNull(taskListBean.getTaskListForm().getTitle());
    }

    @Test
    void testSaveTaskList_CreateNewTaskList() {
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            // Arrange
            TaskListFormDTO formDTO = new TaskListFormDTO();
            formDTO.setTitle("New List");
            formDTO.setDescription("Description");
            taskListBean.setTaskListForm(formDTO);

            User mockUser = new User();
            mockUser.setUserId(1L);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(mockUser);

            // Act
            String result = taskListBean.saveTaskList();

            // Assert
            assertNull(result);
            verify(taskListService, times(1)).insertModel(any(TaskList.class));
        }
    }

    @Test
    void testSaveTaskList_UpdateExistingTaskList() {
        // Arrange
        TaskList existingTaskList = new TaskList();
        existingTaskList.setTitle("Existing List");
        taskListBean.setTaskListEdit(existingTaskList);

        TaskListFormDTO formDTO = new TaskListFormDTO();
        formDTO.setTitle("Updated List");
        formDTO.setDescription("Updated Description");
        taskListBean.setTaskListForm(formDTO);

        // Act
        String result = taskListBean.saveTaskList();

        // Assert
        assertNull(result);
        verify(taskListService, times(1)).updateModel(existingTaskList);
    }

    @Test
    void testSaveTaskList_InvalidData() {
        // Arrange
        TaskListFormDTO formDTO = new TaskListFormDTO();
        formDTO.setTitle(null); // Invalid data
        taskListBean.setTaskListForm(formDTO);

        // Act
        String result = taskListBean.saveTaskList();

        // Assert
        assertNull(result);
        verify(taskListService, never()).insertModel(any(TaskList.class));
        verify(taskListService, never()).updateModel(any(TaskList.class));

        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(mockedFacesContext).addMessage(eq(null), messageCaptor.capture());

        FacesMessage capturedMessage = messageCaptor.getValue();
        assertNotNull(capturedMessage);
        assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
        assertEquals("Save unsuccessful", capturedMessage.getSummary());
        assertEquals("List Name can't be empty", capturedMessage.getDetail());
    }

    @Test
    void testIsValid_ValidData() {
        // Arrange
        TaskListFormDTO formDTO = new TaskListFormDTO();
        formDTO.setTitle("Valid List");
        formDTO.setDescription("Description");
        taskListBean.setTaskListForm(formDTO);

        // Act
        boolean result = taskListBean.isValid();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsValid_InvalidData() {
        // Arrange
        TaskListFormDTO formDTO = new TaskListFormDTO();
        formDTO.setTitle(null); // Invalid data
        taskListBean.setTaskListForm(formDTO);

        // Act
        boolean result = taskListBean.isValid();

        // Assert
        assertFalse(result);

        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(mockedFacesContext).addMessage(eq(null), messageCaptor.capture());

        FacesMessage capturedMessage = messageCaptor.getValue();
        assertNotNull(capturedMessage);
        assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
        assertEquals("Save unsuccessful", capturedMessage.getSummary());
        assertEquals("List Name can't be empty", capturedMessage.getDetail());
    }
}
