package com.ffhs.jeetasks.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * DTO representing the registration form input fields.
 */
@Data
public class RegistrationFormDTO {

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;
}
