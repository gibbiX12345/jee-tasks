package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.Notification;
import com.ffhs.jeetasks.service.NotificationService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Named
@SessionScoped
public class NotificationBean implements Serializable {

    @Inject
    private NotificationService notificationService;
    @Inject
    private LoginBean loginBean;

    private List<Notification> openNotifications;

    public List<Notification> getOpenNotifications() {
        this.openNotifications = notificationService.findAllNotificationsForUserNotDismissed();
        return openNotifications;
    }

    public void loadOpenNotifications() {
        this.openNotifications = notificationService.findAllNotificationsForUserNotDismissed();
    }

    public void dismissNotification(Notification notification) {
        notification.setDismissed(true);
        notificationService.updateModel(notification);
        loadOpenNotifications();
    }
}