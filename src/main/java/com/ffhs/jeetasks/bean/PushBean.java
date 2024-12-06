package com.ffhs.jeetasks.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Calendar;

@ApplicationScoped
@Named
public class PushBean implements Serializable {

    @Inject
    @Push
    private PushContext notificationPushChannel;

    public void sendPushMessage(String message) {
        notificationPushChannel.send(message);
    }

    public void clockAction() {
        Calendar now = Calendar.getInstance();

        String time = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);

        notificationPushChannel.send(time);
    }
}
