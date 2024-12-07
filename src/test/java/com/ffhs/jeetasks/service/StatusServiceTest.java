package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Status;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private StatusService statusService;

    @Mock
    private TypedQuery<Status> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllStatuses_Positive() {
        // Arrange
        Status status1 = new Status();
        status1.setOrder(1);
        status1.setValue("Open");

        Status status2 = new Status();
        status2.setOrder(2);
        status2.setValue("Closed");

        List<Status> statuses = Arrays.asList(status1, status2);

        when(entityManager.createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(statuses);

        // Act
        List<Status> result = statusService.findAllStatuses();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Open", result.get(0).getValue());
        assertEquals("Closed", result.get(1).getValue());
        verify(entityManager, times(1)).createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindAllStatuses_EmptyList() {
        // Arrange
        when(entityManager.createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<Status> result = statusService.findAllStatuses();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(entityManager, times(1)).createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindAllStatuses_Exception() {
        // Arrange
        when(entityManager.createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class)).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, statusService::findAllStatuses);
        assertEquals("Database error", exception.getMessage());
        verify(entityManager, times(1)).createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindAllStatuses_NullResult() {
        // Arrange
        when(entityManager.createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(null);

        // Act
        List<Status> result = statusService.findAllStatuses();

        // Assert
        assertNull(result);
        verify(entityManager, times(1)).createQuery("SELECT s FROM Status s ORDER BY s.order", Status.class);
        verify(query, times(1)).getResultList();
    }
}
