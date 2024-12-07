package com.ffhs.jeetasks.service;

import com.ffhs.jeetasks.entity.Notification;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private TypedQuery<Notification> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllNotificationsForUserNotDismissed_Positive() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        Notification notification1 = new Notification();
        notification1.setText("Notification 1");
        Notification notification2 = new Notification();
        notification2.setText("Notification 2");

        List<Notification> notifications = List.of(notification1, notification2);

        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            mockedSessionUtils.when(SessionUtils::isLoggedIn).thenReturn(true);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(user);

            when(entityManager.createQuery(anyString(), eq(Notification.class))).thenReturn(query);
            when(query.setParameter("userId", user.getUserId())).thenReturn(query);
            when(query.getResultList()).thenReturn(notifications);

            // Act
            List<Notification> result = notificationService.findAllNotificationsForUserNotDismissed();

            // Assert
            assertEquals(2, result.size());
            assertEquals("Notification 1", result.get(0).getText());
            assertEquals("Notification 2", result.get(1).getText());
            verify(entityManager, times(1)).createQuery(anyString(), eq(Notification.class));
            verify(query, times(1)).setParameter("userId", user.getUserId());
        }
    }

    @Test
    void testFindAllNotificationsForUserNotDismissed_NotLoggedIn() {
        // Arrange
        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            mockedSessionUtils.when(SessionUtils::isLoggedIn).thenReturn(false);

            // Act
            List<Notification> result = notificationService.findAllNotificationsForUserNotDismissed();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(entityManager, never()).createQuery(anyString(), eq(Notification.class));
        }
    }

    @Test
    void testFindAllNotificationsForUserNotDismissed_NoNotifications() {
        // Arrange
        User user = new User();
        user.setUserId(1L);

        try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
            mockedSessionUtils.when(SessionUtils::isLoggedIn).thenReturn(true);
            mockedSessionUtils.when(SessionUtils::getLoggedInUser).thenReturn(user);

            when(entityManager.createQuery(anyString(), eq(Notification.class))).thenReturn(query);
            when(query.setParameter("userId", user.getUserId())).thenReturn(query);
            when(query.getResultList()).thenReturn(Collections.emptyList());

            // Act
            List<Notification> result = notificationService.findAllNotificationsForUserNotDismissed();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testCreateNotification_Positive() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        String text = "Test Notification";
        String link = "http://example.com";

        // Act
        notificationService.createNotification(text, user, link);

        // Assert
        verify(entityManager, times(1)).persist(any(Notification.class));
    }

    @Test
    void testCreateNotification_NullRecipient() {
        // Arrange
        String text = "Test Notification";
        String link = "http://example.com";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> notificationService.createNotification(text, null, link));
    }

    @Test
    void testInsertModel() {
        // Arrange
        Notification notification = new Notification();
        notification.setText("Sample Notification");

        // Act
        notificationService.insertModel(notification);

        // Assert
        verify(entityManager, times(1)).persist(notification);
    }

    @Test
    void testUpdateModel() {
        // Arrange
        Notification notification = new Notification();
        notification.setText("Updated Notification");

        // Act
        notificationService.updateModel(notification);

        // Assert
        verify(entityManager, times(1)).merge(notification);
    }
}

