package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
@Named
@RequestScoped
public class RegistrationBean {

    @Inject
    private UserService userService;

    private String email;
    private String password;

    public void registerUser() {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        userService.registerUser(user);
    }
}

