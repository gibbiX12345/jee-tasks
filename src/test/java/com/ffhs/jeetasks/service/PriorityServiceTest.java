package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Priority;
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

class PriorityServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PriorityService priorityService;

    @Mock
    private TypedQuery<Priority> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllPriorities_Positive() {
        // Arrange
        Priority priority1 = new Priority();
        priority1.setOrder(1);
        priority1.setLevel("High");

        Priority priority2 = new Priority();
        priority2.setOrder(2);
        priority2.setLevel("Medium");

        List<Priority> priorities = Arrays.asList(priority1, priority2);

        when(entityManager.createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(priorities);

        // Act
        List<Priority> result = priorityService.findAllPriorities();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("High", result.get(0).getLevel());
        assertEquals("Medium", result.get(1).getLevel());
        verify(entityManager, times(1)).createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindAllPriorities_EmptyList() {
        // Arrange
        when(entityManager.createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<Priority> result = priorityService.findAllPriorities();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(entityManager, times(1)).createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindAllPriorities_Exception() {
        // Arrange
        when(entityManager.createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class)).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, priorityService::findAllPriorities);
        assertEquals("Database error", exception.getMessage());
        verify(entityManager, times(1)).createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindAllPriorities_NullResult() {
        // Arrange
        when(entityManager.createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(null);

        // Act
        List<Priority> result = priorityService.findAllPriorities();

        // Assert
        assertNull(result);
        verify(entityManager, times(1)).createQuery("SELECT p FROM Priority p ORDER BY p.order", Priority.class);
        verify(query, times(1)).getResultList();
    }
}
