package com.ffhs.jeetasks.bean.data;

import com.ffhs.jeetasks.bean.data.PriorityBean;
import com.ffhs.jeetasks.entity.Priority;
import com.ffhs.jeetasks.service.PriorityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriorityBeanTest {

    @Mock
    private PriorityService priorityService;

    @InjectMocks
    private PriorityBean priorityBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPriorities_Positive() {
        // Arrange
        Priority priority1 = new Priority();
        priority1.setOrder(1);
        priority1.setLevel("High");

        Priority priority2 = new Priority();
        priority2.setOrder(2);
        priority2.setLevel("Low");

        List<Priority> priorities = Arrays.asList(priority1, priority2);

        when(priorityService.findAllPriorities()).thenReturn(priorities);

        // Act
        List<Priority> result = priorityBean.getPriorities();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("High", result.get(0).getLevel());
        assertEquals("Low", result.get(1).getLevel());
        verify(priorityService, times(1)).findAllPriorities();
    }

    @Test
    void testGetPriorities_Empty() {
        // Arrange
        when(priorityService.findAllPriorities()).thenReturn(Collections.emptyList());

        // Act
        List<Priority> result = priorityBean.getPriorities();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(priorityService, times(1)).findAllPriorities();
    }

    @Test
    void testGetPriorities_ServiceThrowsException() {
        // Arrange
        when(priorityService.findAllPriorities()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> priorityBean.getPriorities());
        assertEquals("Database error", exception.getMessage());
        verify(priorityService, times(1)).findAllPriorities();
    }
}
