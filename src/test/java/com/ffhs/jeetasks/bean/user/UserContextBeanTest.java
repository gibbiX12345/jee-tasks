package com.ffhs.jeetasks.bean.user;

import com.ffhs.jeetasks.util.SessionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class UserContextBeanTest {

    private UserContextBean userContextBean;

    @BeforeEach
    void setUp() {
        userContextBean = new UserContextBean();
    }

    @Test
    void testGetEmail_UserLoggedIn() {
        // Arrange
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            mockedSessionUtils.when(SessionUtils::getLoggedInUserEmail).thenReturn("user@example.com");

            // Act
            String email = userContextBean.getEmail();

            // Assert
            assertNotNull(email);
            assertEquals("user@example.com", email);
        }
    }

    @Test
    void testGetEmail_NoUserLoggedIn() {
        // Arrange
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            mockedSessionUtils.when(SessionUtils::getLoggedInUserEmail).thenReturn(null);

            // Act
            String email = userContextBean.getEmail();

            // Assert
            assertNull(email);
        }
    }

    @Test
    void testGetEmail_EmptyEmail() {
        // Arrange
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            mockedSessionUtils.when(SessionUtils::getLoggedInUserEmail).thenReturn("");

            // Act
            String email = userContextBean.getEmail();

            // Assert
            assertNotNull(email);
            assertEquals("", email);
        }
    }

    @Test
    void testGetEmail_ExceptionThrown() {
        // Arrange
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            mockedSessionUtils.when(SessionUtils::getLoggedInUserEmail).thenThrow(new RuntimeException("Session error"));

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> userContextBean.getEmail());
            assertEquals("Session error", exception.getMessage());
        }
    }
}
