package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
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

    private String email;
    private String password;
    private Long userId;

    public String login() {
        Optional<User> user = userService.findUserByEmail(email);

        if (user.isPresent() && BCrypt.checkpw(password, user.get().getPasswordHash())) {
            this.userId = user.get().getUserId();
            return "/taskList?faces-redirect=true";
        } else {
            return "/login?faces-redirect=true";
        }
    }

    public void checkLogin() throws IOException {
        if (userId == null) {
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
