package com.ffhs.jeetasks.bean.user;

import com.ffhs.jeetasks.bean.user.LoginBean;
import com.ffhs.jeetasks.dto.LoginFormDTO;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginBeanTest {

    @InjectMocks
    private LoginBean loginBean;

    @Mock
    private UserService userService;

    private FacesContext mockedFacesContext;
    private MockedStatic<FacesContext> mockedStaticFacesContext;
    private MockedStatic<SessionUtils> mockedStaticSessionUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock FacesContext
        mockedFacesContext = mock(FacesContext.class, RETURNS_DEEP_STUBS);
        mockedStaticFacesContext = mockStatic(FacesContext.class);
        mockedStaticFacesContext.when(FacesContext::getCurrentInstance).thenReturn(mockedFacesContext);

        // Mock SessionUtils
        mockedStaticSessionUtils = mockStatic(SessionUtils.class);
    }

    @AfterEach
    void tearDown() {
        // Close mocked static contexts to avoid conflicts
        if (mockedStaticFacesContext != null) {
            mockedStaticFacesContext.close();
        }
        if (mockedStaticSessionUtils != null) {
            mockedStaticSessionUtils.close();
        }
    }

    @Test
    void testLogin_ValidCredentials() {
        // Arrange
        LoginFormDTO loginForm = new LoginFormDTO();
        loginForm.setEmail("user@example.com");
        loginForm.setPassword("password");
        loginBean.setLoginForm(loginForm);

        User user = new User();
        user.setEmail("user@example.com");
        user.setPasswordHash(BCrypt.hashpw("password", BCrypt.gensalt()));

        when(userService.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        String result = loginBean.login();

        // Assert
        assertEquals("/index?faces-redirect=true", result);
        verify(userService, times(1)).findUserByEmail("user@example.com");
        mockedStaticSessionUtils.verify(() -> SessionUtils.setLoggedInUser(user), times(1));
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        LoginFormDTO loginForm = new LoginFormDTO();
        loginForm.setEmail("user@example.com");
        loginForm.setPassword("wrongpassword");
        loginBean.setLoginForm(loginForm);

        User user = new User();
        user.setEmail("user@example.com");
        user.setPasswordHash(BCrypt.hashpw("password", BCrypt.gensalt()));

        when(userService.findUserByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        String result = loginBean.login();

        // Assert
        assertNull(result);
        verify(userService, times(1)).findUserByEmail("user@example.com");

        ArgumentCaptor<FacesMessage> messageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
        verify(mockedFacesContext).addMessage(eq(null), messageCaptor.capture());

        FacesMessage capturedMessage = messageCaptor.getValue();
        assertNotNull(capturedMessage);
        assertEquals(FacesMessage.SEVERITY_ERROR, capturedMessage.getSeverity());
        assertEquals("Login unsuccessful", capturedMessage.getSummary());
        assertEquals("Invalid email or password", capturedMessage.getDetail());
    }

    @Test
    void testCheckLogin_UserLoggedIn() throws IOException {
        // Arrange
        when(SessionUtils.isLoggedIn()).thenReturn(true);

        // Act
        loginBean.checkLogin();

        // Assert
        verify(mockedFacesContext.getExternalContext(), never()).redirect(anyString());
    }

    @Test
    void testCheckLogin_UserNotLoggedIn() throws IOException {
        // Arrange
        when(SessionUtils.isLoggedIn()).thenReturn(false);

        // Act
        loginBean.checkLogin();

        // Assert
        verify(mockedFacesContext.getExternalContext(), times(1)).redirect("login.xhtml");
    }

    @Test
    void testLogout() {
        // Arrange
        HttpSession mockedSession = mock(HttpSession.class);
        when(mockedFacesContext.getExternalContext().getSession(false)).thenReturn(mockedSession);

        // Act
        String result = loginBean.logout();

        // Assert
        assertEquals("/login?faces-redirect=true", result);
        verify(mockedSession, times(1)).invalidate();
    }

    @Test
    void testLogout_NoActiveSession() {
        // Arrange
        when(mockedFacesContext.getExternalContext().getSession(false)).thenReturn(null);

        // Act
        String result = loginBean.logout();

        // Assert
        assertEquals("/login?faces-redirect=true", result);
    }
}
