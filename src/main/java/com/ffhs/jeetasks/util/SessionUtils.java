package com.ffhs.jeetasks.util;

import com.ffhs.jeetasks.entity.User;
import jakarta.faces.context.FacesContext;

public abstract class SessionUtils {

    private SessionUtils() {
        // Utility class, no instantiation
    }

    public static User getLoggedInUser() {
        return (User) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loggedInUser");
    }

    public static String getLoggedInUserEmail() {
        User user = getLoggedInUser();
        return user != null ? user.getEmail() : null;
    }

    public static boolean isLoggedIn() {
        return getLoggedInUser() != null;
    }

    public static void setLoggedInUser(User user) {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("loggedInUser", user);
    }
}
