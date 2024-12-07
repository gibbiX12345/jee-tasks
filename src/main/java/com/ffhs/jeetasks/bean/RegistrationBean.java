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

/**
 * Bean responsible for user registration functionality.
 */
@Data
@Named
@RequestScoped
public class RegistrationBean {

    @Inject
    private UserService userService;

    /**
     * User email for registration. Must be non-empty and in a valid email format.
     */
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    /**
     * User password for registration. Must be non-empty.
     */
    @NotEmpty(message = "Password is required")
    private String password;

    /**
     * Registers a new user with the provided email and password.
     * The password is securely hashed before storing in the database.
     *
     * @return Navigation string to redirect to the login page after successful registration.
     */
    public String registerUser() {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        userService.registerUser(user);
        return "/login?faces-redirect=true";
    }
}
