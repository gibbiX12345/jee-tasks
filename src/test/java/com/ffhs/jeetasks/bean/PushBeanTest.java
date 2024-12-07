package com.ffhs.jeetasks.bean;

import jakarta.faces.push.PushContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PushBeanTest {

    @InjectMocks
    private PushBean pushBean;

    @Mock
    private PushContext notificationPushChannel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendPushMessage_Success() {
        // Arrange
        String message = "Test message";

        // Act
        pushBean.sendPushMessage(message);

        // Assert
        verify(notificationPushChannel, times(1)).send(message);
    }

    @Test
    void testSendPushMessage_NullMessage() {
        // Arrange
        String message = null;

        // Act
        pushBean.sendPushMessage(message);

        // Assert
        verify(notificationPushChannel, times(1)).send(message);
    }

    @Test
    void testSendPushMessage_EmptyMessage() {
        // Arrange
        String message = "";

        // Act
        pushBean.sendPushMessage(message);

        // Assert
        verify(notificationPushChannel, times(1)).send(message);
    }

    @Test
    void testSendPushMessage_ExceptionThrown() {
        // Arrange
        String message = "Test message";
        doThrow(new RuntimeException("Push channel error"))
                .when(notificationPushChannel).send(message);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> pushBean.sendPushMessage(message));
        assertEquals("Push channel error", exception.getMessage());
    }
}