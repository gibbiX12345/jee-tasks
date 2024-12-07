package com.ffhs.jeetasks.util;

import com.ffhs.jeetasks.entity.User;
import jakarta.faces.context.FacesContext;

/**
 * Utility class for managing session-related operations, particularly for the logged-in user.
 */
public abstract class SessionUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SessionUtils() {
        // Utility class, no instantiation
    }

    /**
     * Retrieves the currently logged-in user from the session.
     *
     * @return The {@link User} object of the logged-in user, or {@code null} if no user is logged in.
     */
    public static User getLoggedInUser() {
        return (User) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loggedInUser");
    }

    /**
     * Retrieves the email of the currently logged-in user.
     *
     * @return The email address of the logged-in user, or {@code null} if no user is logged in.
     */
    public static String getLoggedInUserEmail() {
        User user = getLoggedInUser();
        return user != null ? user.getEmail() : null;
    }

    /**
     * Checks whether a user is currently logged in.
     *
     * @return {@code true} if a user is logged in, {@code false} otherwise.
     */
    public static boolean isLoggedIn() {
        return getLoggedInUser() != null;
    }

    /**
     * Sets the logged-in user in the session.
     *
     * @param user The {@link User} object to be set as the logged-in user.
     */
    public static void setLoggedInUser(User user) {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("loggedInUser", user);
    }
}
