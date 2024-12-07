package com.ffhs.jeetasks.bean.user;

import com.ffhs.jeetasks.dto.RegistrationFormDTO;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationBeanTest {

    @InjectMocks
    private RegistrationBean registrationBean;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        RegistrationFormDTO registrationForm = new RegistrationFormDTO();
        registrationForm.setEmail("test@example.com");
        registrationForm.setPassword("password123");
        registrationBean.setRegistrationForm(registrationForm);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Act
        String result = registrationBean.registerUser();

        // Assert
        assertEquals("/login?faces-redirect=true", result);
        verify(userService, times(1)).registerUser(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("test@example.com", capturedUser.getEmail());
        assertTrue(BCrypt.checkpw("password123", capturedUser.getPasswordHash()));
    }

    @Test
    void testRegisterUser_EmptyEmail() {
        // Arrange
        RegistrationFormDTO registrationForm = new RegistrationFormDTO();
        registrationForm.setEmail("");
        registrationForm.setPassword("password123");
        registrationBean.setRegistrationForm(registrationForm);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, registrationBean::registerUser);
        assertEquals("Email cannot be empty", exception.getMessage());

        verify(userService, never()).registerUser(any(User.class));
    }

    @Test
    void testRegisterUser_EmptyPassword() {
        // Arrange
        RegistrationFormDTO registrationForm = new RegistrationFormDTO();
        registrationForm.setEmail("test@example.com");
        registrationForm.setPassword("");
        registrationBean.setRegistrationForm(registrationForm);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, registrationBean::registerUser);
        assertEquals("Password cannot be empty", exception.getMessage());

        verify(userService, never()).registerUser(any(User.class));
    }

    @Test
    void testRegisterUser_NullEmail() {
        // Arrange
        RegistrationFormDTO registrationForm = new RegistrationFormDTO();
        registrationForm.setEmail(null);
        registrationForm.setPassword("password123");
        registrationBean.setRegistrationForm(registrationForm);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, registrationBean::registerUser);
        assertEquals("Email cannot be null", exception.getMessage());

        verify(userService, never()).registerUser(any(User.class));
    }

    @Test
    void testRegisterUser_NullPassword() {
        // Arrange
        RegistrationFormDTO registrationForm = new RegistrationFormDTO();
        registrationForm.setEmail("test@example.com");
        registrationForm.setPassword(null);
        registrationBean.setRegistrationForm(registrationForm);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, registrationBean::registerUser);
        assertEquals("Password cannot be null", exception.getMessage());

        verify(userService, never()).registerUser(any(User.class));
    }
}