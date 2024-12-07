package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

/**
 * Bean responsible for managing user-related operations in the application.
 */
@Named
@RequestScoped
public class UserBean {

    @Inject
    private UserService userService;

    /**
     * Retrieves all users available in the system.
     *
     * @return A list of all {@link User} entities.
     */
    public List<User> getUsers() {
        return userService.findAllUsers();
    }
}
