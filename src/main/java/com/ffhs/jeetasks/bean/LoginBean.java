package com.ffhs.jeetasks.bean;

import com.ffhs.jeetasks.dto.LoginFormDTO;
import com.ffhs.jeetasks.entity.User;
import com.ffhs.jeetasks.service.UserService;
import com.ffhs.jeetasks.util.SessionUtils;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * Bean responsible for handling user authentication and session management.
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private UserService userService;

    @Getter
    @Setter
    private LoginFormDTO loginForm = new LoginFormDTO();

    /**
     * Handles user login by validating credentials and setting session state.
     *
     * @return Navigation string for redirection.
     */
    public String login() {
        Optional<User> userOptional = userService.findUserByEmail(loginForm.getEmail());
        if (userOptional.isPresent() && BCrypt.checkpw(loginForm.getPassword(), userOptional.get().getPasswordHash())) {
            SessionUtils.setLoggedInUser(userOptional.get());
            return "/index?faces-redirect=true";
        } else {
            addErrorMessage("Login unsuccessful", "Invalid email or password");
            return null;
        }
    }


    /**
     * Ensures that only logged-in users can access specific pages.
     *
     * @throws IOException if the user is not logged in.
     */
    public void checkLogin() throws IOException {
        if (!SessionUtils.isLoggedIn()) {
            redirect("login.xhtml");
        }
    }

    /**
     * Logs out the current user and invalidates the session.
     *
     * @return Navigation string for redirection.
     */
    public String logout() {
        invalidateSession();
        return "/login?faces-redirect=true";
    }

    /**
     * Adds a FacesMessage to the current context.
     *
     * @param summary Summary of the message.
     * @param detail  Detailed description of the message.
     */
    private void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    /**
     * Redirects to a specified page.
     *
     * @param page The page to redirect to.
     * @throws IOException if the redirection fails.
     */
    private void redirect(String page) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(page);
    }

    /**
     * Invalidates the current session.
     */
    private void invalidateSession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
