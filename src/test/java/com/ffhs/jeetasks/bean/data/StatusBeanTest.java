package com.ffhs.jeetasks.bean.data;

import com.ffhs.jeetasks.entity.Status;
import com.ffhs.jeetasks.service.StatusService;
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

class StatusBeanTest {

    @Mock
    private StatusService statusService;

    @InjectMocks
    private StatusBean statusBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStatuses_Positive() {
        // Arrange
        Status status1 = new Status();
        status1.setValue("Open");
        Status status2 = new Status();
        status2.setValue("Closed");

        List<Status> statuses = Arrays.asList(status1, status2);

        when(statusService.findAllStatuses()).thenReturn(statuses);

        // Act
        List<Status> result = statusBean.getStatuses();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Open", result.get(0).getValue());
        assertEquals("Closed", result.get(1).getValue());
        verify(statusService, times(1)).findAllStatuses();
    }

    @Test
    void testGetStatuses_Empty() {
        // Arrange
        when(statusService.findAllStatuses()).thenReturn(Collections.emptyList());

        // Act
        List<Status> result = statusBean.getStatuses();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statusService, times(1)).findAllStatuses();
    }

    @Test
    void testGetStatuses_Exception() {
        // Arrange
        when(statusService.findAllStatuses()).thenThrow(new RuntimeException("Database error"));

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> statusBean.getStatuses());

        // Assert
        assertNotNull(exception);
        assertEquals("Database error", exception.getMessage());
        verify(statusService, times(1)).findAllStatuses();
    }
}
