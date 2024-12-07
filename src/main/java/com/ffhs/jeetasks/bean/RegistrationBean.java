package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
@Named
@RequestScoped
public class RegistrationBean {

    @Inject
    private UserService userService;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @NotEmpty(message = "Password is required")
    private String password;

    public String registerUser() {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        userService.registerUser(user);
        return "/login?faces-redirect=true";
    }
}

