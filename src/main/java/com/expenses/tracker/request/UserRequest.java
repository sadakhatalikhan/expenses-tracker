package com.expenses.tracker.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserRequest (
    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Phone Number is required")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Phone number must contain 10 digits"
    )
    String phoneNumber,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 10,
            message = "Password must contain 8 to 10 characters")
    String password
){}