package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.User;
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
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserService userService;

    @Mock
    private TypedQuery<User> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllUsers_Positive() {
        // Arrange
        User user1 = new User();
        user1.setUserId(1L);
        User user2 = new User();
        user2.setUserId(2L);

        List<User> users = Arrays.asList(user1, user2);

        when(entityManager.createQuery("SELECT u FROM User u", User.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(users);

        // Act
        List<User> result = userService.findAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(2L, result.get(1).getUserId());
        verify(entityManager, times(1)).createQuery("SELECT u FROM User u", User.class);
    }

    @Test
    void testFindAllUsers_Empty() {
        // Arrange
        when(entityManager.createQuery("SELECT u FROM User u", User.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userService.findAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindUserByEmail_Positive() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");

        when(entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)).thenReturn(query);
        when(query.setParameter("email", "test@example.com")).thenReturn(query);
        when(query.getResultStream()).thenReturn(Arrays.stream(new User[]{user}));

        // Act
        Optional<User> result = userService.findUserByEmail("test@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testFindUserByEmail_NotFound() {
        // Arrange
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)).thenReturn(query);
        when(query.setParameter("email", "test@example.com")).thenReturn(query);
        when(query.getResultStream()).thenAnswer(invocation -> Stream.empty());

        // Act
        Optional<User> result = userService.findUserByEmail("test@example.com");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindUserById_Positive() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class)).thenReturn(query);
        when(query.setParameter("userId", 1L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(user);

        // Act
        User result = userService.findUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    void testFindUserById_NotFound() {
        // Arrange
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class)).thenReturn(query);
        when(query.setParameter("userId", 1L)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new jakarta.persistence.NoResultException());

        // Act
        Exception exception = assertThrows(jakarta.persistence.NoResultException.class, () -> userService.findUserById(1L));

        // Assert
        assertNotNull(exception);
    }

    @Test
    void testRegisterUser() {
        // Arrange
        User user = new User();

        // Act
        userService.registerUser(user);

        // Assert
        verify(entityManager, times(1)).persist(user);
    }
}
