package com.babakalizada.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoRegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, message = "New Password must be not empty!")
    private String newPassword;
    @NotBlank
    @Size(min = 8, message = "Confirm Password must be not empty!")
    private String confirmPassword;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
