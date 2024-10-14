package com.savolodya.predictiveportfolio.models.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserForm(
        @Size(min = 3, max = 255, message = "Email has to be between 3 and 255 characters")
        @Email(message = "Email is not valid")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password does not match rules")
        String password
) {}
