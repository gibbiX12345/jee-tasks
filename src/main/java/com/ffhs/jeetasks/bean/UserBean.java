package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class UserBean {

    @Inject
    private UserService userService;

    public List<User> getUsers() {
        return userService.findAllUsers();
    }
}