package com.ffhs.jeetasks.bean.user;

import com.ffhs.jeetasks.bean.user.UserBean;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
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

class UserBeanTest {

    @InjectMocks
    private UserBean userBean;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers_WithUsers() {
        // Arrange
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        List<User> users = Arrays.asList(user1, user2);
        when(userService.findAllUsers()).thenReturn(users);

        // Act
        List<User> result = userBean.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());

        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void testGetUsers_NoUsers() {
        // Arrange
        when(userService.findAllUsers()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userBean.getUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void testGetUsers_Exception() {
        // Arrange
        when(userService.findAllUsers()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, userBean::getUsers);
        assertEquals("Database error", exception.getMessage());

        verify(userService, times(1)).findAllUsers();
    }
}
