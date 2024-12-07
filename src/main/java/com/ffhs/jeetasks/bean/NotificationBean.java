package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Notification;
import com.ffhs.jeetasks.service.NotificationService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Bean responsible for managing user notifications.
 */
@Named
@SessionScoped
public class NotificationBean implements Serializable {

    @Inject
    private NotificationService notificationService;

    private List<Notification> openNotifications;

    /**
     * Retrieves all open (non-dismissed) notifications for the logged-in user.
     *
     * @return List of open notifications.
     */
    public List<Notification> getOpenNotifications() {
        if (openNotifications == null) {
            loadOpenNotifications();
        }
        return openNotifications;
    }

    /**
     * Loads open (non-dismissed) notifications for the logged-in user into the bean.
     */
    public void loadOpenNotifications() {
        this.openNotifications = notificationService.findAllNotificationsForUserNotDismissed();
    }

    /**
     * Marks a notification as dismissed and updates the notification list.
     *
     * @param notification The notification to dismiss.
     */
    public void dismissNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }

        notification.setDismissed(true);
        notificationService.updateModel(notification);
        loadOpenNotifications();
    }
}
