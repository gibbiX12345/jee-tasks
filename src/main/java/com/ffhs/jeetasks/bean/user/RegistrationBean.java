package com.ffhs.jeetasks.bean.user;

import com.ffhs.jeetasks.dto.LoginFormDTO;
import com.ffhs.jeetasks.dto.RegistrationFormDTO;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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

    @Getter
    @Setter
    private RegistrationFormDTO registrationForm = new RegistrationFormDTO();

    /**
     * Registers a new user with the provided email and password.
     * The password is securely hashed before storing in the database.
     *
     * @return Navigation string to redirect to the login page after successful registration.
     */
    public String registerUser() {
        User user = new User();
        user.setEmail(registrationForm.getEmail());
        user.setPasswordHash(BCrypt.hashpw(registrationForm.getPassword(), BCrypt.gensalt()));
        userService.registerUser(user);
        return "/login?faces-redirect=true";
    }
}
