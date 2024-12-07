package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@Data
@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private UserService userService;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @NotEmpty(message = "Password is required")
    private String password;
    private User user;

    public String login() {
        Optional<User> user = userService.findUserByEmail(email);

        if (user.isPresent() && BCrypt.checkpw(password, user.get().getPasswordHash())) {
            this.user = user.get();
            return "/index?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login unsuccessful", "Invalid email or password"));
            return null;
        }
    }

    public void checkLogin() throws IOException {
        if (user == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "/login?faces-redirect=true";
    }
}
