package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * Bean responsible for providing user context within the session scope.
 */
@Named
@SessionScoped
public class UserContextBean implements Serializable {

    /**
     * Retrieves the email address of the currently logged-in user from the session.
     *
     * @return The email of the logged-in user, or null if no user is logged in.
     */
    public String getEmail() {
        return SessionUtils.getLoggedInUserEmail();
    }
}
