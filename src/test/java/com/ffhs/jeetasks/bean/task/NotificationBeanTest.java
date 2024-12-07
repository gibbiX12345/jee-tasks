package com.ffhs.jeetasks.bean.task;

import com.ffhs.jeetasks.entity.Notification;
import com.ffhs.jeetasks.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationBeanTest {

    @InjectMocks
    private NotificationBean notificationBean;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOpenNotifications_Positive() {
        // Arrange
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationService.findAllNotificationsForUserNotDismissed()).thenReturn(notifications);

        // Act
        List<Notification> result = notificationBean.getOpenNotifications();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationService, times(1)).findAllNotificationsForUserNotDismissed();
    }

    @Test
    void testGetOpenNotifications_Empty() {
        // Arrange
        when(notificationService.findAllNotificationsForUserNotDismissed()).thenReturn(new ArrayList<>());

        // Act
        List<Notification> result = notificationBean.getOpenNotifications();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationService, times(1)).findAllNotificationsForUserNotDismissed();
    }

    @Test
    void testLoadOpenNotifications_Positive() {
        // Arrange
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationService.findAllNotificationsForUserNotDismissed()).thenReturn(notifications);

        // Act
        notificationBean.loadOpenNotifications();
        List<Notification> result = notificationBean.getOpenNotifications();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationService, times(1)).findAllNotificationsForUserNotDismissed();
    }

    @Test
    void testDismissNotification_Positive() {
        // Arrange
        Notification notification = new Notification();
        notification.setDismissed(false);

        doNothing().when(notificationService).updateModel(notification);

        // Act
        notificationBean.dismissNotification(notification);

        // Assert
        assertTrue(notification.isDismissed());
        verify(notificationService, times(1)).updateModel(notification);
        verify(notificationService, times(1)).findAllNotificationsForUserNotDismissed();
    }

    @Test
    void testDismissNotification_NullNotification() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> notificationBean.dismissNotification(null));
        assertEquals("Notification cannot be null", exception.getMessage());
        verify(notificationService, never()).updateModel(any(Notification.class));
        verify(notificationService, never()).findAllNotificationsForUserNotDismissed();
    }

    @Test
    void testGetOpenNotifications_Cached() {
        // Arrange
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationService.findAllNotificationsForUserNotDismissed()).thenReturn(notifications);

        // Act
        List<Notification> firstCall = notificationBean.getOpenNotifications();
        List<Notification> secondCall = notificationBean.getOpenNotifications();

        // Assert
        assertNotNull(firstCall);
        assertEquals(2, firstCall.size());
        assertSame(firstCall, secondCall);
        verify(notificationService, times(1)).findAllNotificationsForUserNotDismissed();
    }
}
