package com.babakalizada.user.dto.request;

import com.babakalizada.user.enums.UserRole;
import com.babakalizada.user.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoUserRequest {

    @NotBlank(message = "Username can't be empty!")
    private String username;

    @NotBlank(message = "Password can't be empty!")
    @Size(min = 7, max = 12, message = "Password length must be between 7 and 12!")
    private String password;

    @Email
    @NotBlank(message = "Email can't be empty!")
    private String email;

    @NotBlank(message = "First Name can't be empty!")
    private String firstName;

    @NotBlank(message = "Last Name can't be empty!")
    private String lastName;

    private UserRole role = UserRole.USER;

    private UserStatus status = UserStatus.PENDING;

}
