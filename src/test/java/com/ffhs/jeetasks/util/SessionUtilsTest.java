package com.ffhs.jeetasks.util;

import com.ffhs.jeetasks.entity.User;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionUtilsTest {

    private FacesContext facesContextMock;
    private ExternalContext externalContextMock;
    private Map<String, Object> sessionMap;
    private MockedStatic<FacesContext> mockedStaticFacesContext;

    @BeforeEach
    void setUp() {
        facesContextMock = mock(FacesContext.class);
        externalContextMock = mock(ExternalContext.class);
        sessionMap = new HashMap<>();

        when(facesContextMock.getExternalContext()).thenReturn(externalContextMock);
        when(externalContextMock.getSessionMap()).thenReturn(sessionMap);

        mockedStaticFacesContext = mockStatic(FacesContext.class);
        mockedStaticFacesContext.when(FacesContext::getCurrentInstance).thenReturn(facesContextMock);
    }

    @AfterEach
    void tearDown() {
        if (mockedStaticFacesContext != null) {
            mockedStaticFacesContext.close();
        }
    }

    @Test
    void testGetLoggedInUser_UserExists() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        sessionMap.put("loggedInUser", user);

        // Act
        User result = SessionUtils.getLoggedInUser();

        // Assert
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void testGetLoggedInUser_NoUser() {
        // Act
        User result = SessionUtils.getLoggedInUser();

        // Assert
        assertNull(result);
    }

    @Test
    void testGetLoggedInUserEmail_UserExists() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        sessionMap.put("loggedInUser", user);

        // Act
        String email = SessionUtils.getLoggedInUserEmail();

        // Assert
        assertNotNull(email);
        assertEquals("user@example.com", email);
    }

    @Test
    void testGetLoggedInUserEmail_NoUser() {
        // Act
        String email = SessionUtils.getLoggedInUserEmail();

        // Assert
        assertNull(email);
    }

    @Test
    void testIsLoggedIn_UserExists() {
        // Arrange
        User user = new User();
        sessionMap.put("loggedInUser", user);

        // Act
        boolean loggedIn = SessionUtils.isLoggedIn();

        // Assert
        assertTrue(loggedIn);
    }

    @Test
    void testIsLoggedIn_NoUser() {
        // Act
        boolean loggedIn = SessionUtils.isLoggedIn();

        // Assert
        assertFalse(loggedIn);
    }

    @Test
    void testSetLoggedInUser() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");

        // Act
        SessionUtils.setLoggedInUser(user);

        // Assert
        assertEquals(user, sessionMap.get("loggedInUser"));
    }

    @Test
    void testSetLoggedInUser_Null() {
        // Act
        SessionUtils.setLoggedInUser(null);

        // Assert
        assertNull(sessionMap.get("loggedInUser"));
    }
}
