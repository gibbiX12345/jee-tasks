package com.ffhs.jeetasks.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * Bean responsible for managing server push notifications using WebSocket channels.
 */
@ApplicationScoped
@Named
public class PushBean implements Serializable {

    @Inject
    @Push
    private PushContext notificationPushChannel;

    /**
     * Sends a push notification message through the WebSocket channel.
     *
     * @param message The message to send to connected clients.
     */
    public void sendPushMessage(String message) {
        notificationPushChannel.send(message);
    }
}
